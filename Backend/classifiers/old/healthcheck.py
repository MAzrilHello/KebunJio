from flask import Flask, request, jsonify
import torch
import torch.nn as nn
import torch.nn.functional as F
from torchvision import transforms
from PIL import Image
import io

class PlantHealthCheckCNN(nn.Module):
    def __init__(self):
        super(PlantHealthCheckCNN, self).__init__()
        
        self.conv1 = nn.Conv2d(in_channels=3, out_channels=32, kernel_size=3, stride=1, padding=1)
        self.conv2 = nn.Conv2d(in_channels=32, out_channels=64, kernel_size=3, stride=1, padding=1)
        self.conv3 = nn.Conv2d(in_channels=64, out_channels=128, kernel_size=3, stride=1, padding=1)
        
        self.fc1 = nn.Linear(128 * 16 * 16, 512)  
        self.fc2 = nn.Linear(512, 2)  # Output 2 classes: Healthy, Diseased

    def forward(self, x):
        x = F.relu(self.conv1(x))
        x = F.max_pool2d(x, 2)
        x = F.relu(self.conv2(x))
        x = F.max_pool2d(x, 2)
        x = F.relu(self.conv3(x))
        x = F.max_pool2d(x, 2)
        x = torch.flatten(x, start_dim=1)
        x = F.relu(self.fc1(x))
        x = self.fc2(x)
        return x

# Initialize the model
model = PlantHealthCheckCNN()

# Load the state dictionary (model weights)
state_dict = torch.load("plant_health_check_final.pth", map_location=torch.device('cpu'))

# Print the keys in the state_dict for debugging
print("State dict keys:", state_dict.keys())
print("Model architecture:", model)  

# Handle mismatched keys (in case it was saved using DataParallel)
if list(state_dict.keys())[0].startswith("module."):
    state_dict = {key.replace("module.", ""): value for key, value in state_dict.items()}

# Load the state dictionary into the model
model.load_state_dict(state_dict)

print("Model architecture after loading state dict:", model)

# Set the model to evaluation mode
# model.eval()

# Preprocessing transforms
valid_transforms = transforms.Compose([
    transforms.Resize((128, 128)),  # Resize image to (128, 128)
    transforms.ToTensor(),  # Convert image to Tensor
    transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225]),  # Normalize using ImageNet values
])

# Flask API
app = Flask(__name__)

@app.route('/api/diagnose', methods=['POST'])
def diagnose():
    file = request.files.get('file')
    if not file:
        return jsonify({"error": "No file uploaded"}), 400

    try:
        image = Image.open(io.BytesIO(file.read())).convert("RGB")  # Ensure the image is in RGB format
        image = valid_transforms(image).unsqueeze(0)  # Apply the transforms and add batch dimension

        with torch.no_grad():
            output = model(image)
            _, predicted = torch.max(output, 1)

        classes = ["Healthy", "Not Healthy"]
        diagnosis = classes[predicted.item()]

        return jsonify({"status": diagnosis, "details": "Model prediction successful"}), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    from waitress import serve
    serve(app, host="0.0.0.0", port=5000)
