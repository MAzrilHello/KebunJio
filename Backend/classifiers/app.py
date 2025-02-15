# -*- coding: utf-8 -*-
"""
Created on Wed Jan 22 13:20:23 2025

@author: Marquan
"""

from flask import Flask, request, jsonify
import torch
import torchvision.models as models
import pickle
from ImgLoading import Compose,ImageReader,Position,make_standard_image
import torchvision.transforms as transforms
import json
from PIL import Image
import io

import torch.nn as nn
import torch.nn.functional as F

   

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



app = Flask(__name__)



@app.route('/')       
def hello(): 
    return 'HELLO'


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


@app.route('/predictSpecies', methods=['GET','POST'])
def predictSpecies():
    #ret_string = 'Reading...'
    epoch_data = open('epoch_19_small.pkl','rb')
    status_dict = pickle.load(epoch_data)
    epoch_data.close()

    IMAGE_SIZE = status_dict['image_size']
    HAVE_LABEL = status_dict['data_labels']
    FRAME_TYPE = status_dict['frame_type']
    ON_SQUARE = status_dict['on_square']
    X_POS = status_dict['x_pos']
    Y_POS = status_dict['y_pos']


    test_transform = Compose([
                              transforms.Resize((IMAGE_SIZE, IMAGE_SIZE)),
                              transforms.ToTensor(),
                            ])
    
    
    
    predictor = models.resnet18(num_classes = len(HAVE_LABEL))
    predictor.load_state_dict(status_dict['model_weights'])
    predictor.eval()
    #pred = predictor(image)
    #return ret_string
    to_return = {'classes':HAVE_LABEL,'error':"No error",'prediction':"No prediction"}
    
    # actual classifying
    if 'file' not in request.files:
        to_return['error']='No file part'
        return jsonify(to_return), 400
    
    file = request.files['file']
    if file.filename == '':
        to_return['error']='No selected file'
        return jsonify(to_return), 400

    # Open the image
    image = Image.open(io.BytesIO(file.read()))
    image = make_standard_image(image,False,ON_SQUARE,X_POS,Y_POS,FRAME_TYPE,None)
    image, _ = test_transform(image, [])
    
    pred = predictor(image.unsqueeze(0))
    pred_label = torch.topk(pred,1).indices.squeeze(1)
    #print(pred_label.item())
    to_return['prediction'] = HAVE_LABEL[pred_label.item()]
    
    return json.dumps(to_return)

if __name__=='__main__': 
   app.run()