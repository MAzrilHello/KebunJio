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

   
app = Flask(__name__)



@app.route('/')       
def hello(): 
    return 'HELLO'


#@app.route('/someapi')
#def index():
#    return json.dumps({'status':'success'})


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