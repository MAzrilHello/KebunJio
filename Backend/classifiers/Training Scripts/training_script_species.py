# -*- coding: utf-8 -*-
"""
Created on Mon Jan 20 12:56:49 2025

@author: TheMarksman
"""


import torchvision.transforms as transforms
import torch
import torchvision.models as models
from torch.utils.data import DataLoader
import numpy as np
import random
from tqdm import tqdm
import torch.optim as optim
import pickle
import pandas as pd

from ImgLoading import Compose, ImageReader,Position
from TrainScripts import seed_worker,do_one_epoch,collate_fn,calculate_overall_means,precision_recall_from_combo








if __name__ == '__main__':
    SEED = 41 # random seed
    IMAGE_SIZE = 224 # size of the image
    LABEL_DIR = "Data Labels.csv"
    # LABEL_DIR is the directory to a csv file. It must have the following columns:
        # Dataset: 'train' or 'test'. Must correspond to values in the 'which_dataset'
        # argument
        # File Name: name of the image file
        # Have <X>: X is the name of every label. Value must be either True or False.
        
    HAVE_LABEL = ['Chili','Cucumber','Tomato','Lettuce','Basil'] # list of columns to read to get the labels
    IMAGE_DIRECTORY = "" # replace with directory containing the images
    TO_REVERSE = False
    FRAME_TYPE = ['RGB']
    ON_SQUARE = True
    X_POS = Position.MIDDLE
    Y_POS = Position.MIDDLE
    
    BATCH_SIZE = 23
    LEARNING_RATE=0.009
    
    device = 'cuda' if torch.cuda.is_available() else 'cpu'
    
    # create the image readers
    torch.manual_seed(SEED)
    train_transform = Compose([
       transforms.ColorJitter(), # adds noise. Only needed for train functions.
       transforms.Resize((IMAGE_SIZE, IMAGE_SIZE)), # resizes a pil image
       transforms.ToTensor(), # converts image from pil image to pytorch tensor
    ])
    trainReader = ImageReader(LABEL_DIR,HAVE_LABEL,IMAGE_DIRECTORY,"Train",train_transform,
                              TO_REVERSE,FRAME_TYPE,ON_SQUARE,X_POS,Y_POS)
    test_transform = Compose([
                              transforms.Resize((IMAGE_SIZE, IMAGE_SIZE)),
                              transforms.ToTensor(),
                            ])
    testReader = ImageReader(LABEL_DIR,HAVE_LABEL,IMAGE_DIRECTORY,"Test",test_transform,
                              TO_REVERSE,FRAME_TYPE,ON_SQUARE,X_POS,Y_POS)
   
    # create the model
    torch.manual_seed(SEED)
    predictor = models.resnet18(num_classes = len(HAVE_LABEL))
    predictor.eval()
    predictor(testReader[0][0].unsqueeze(0))
    predictor.to(device)
    
    # create the loaders
    g = torch.Generator()
    g.manual_seed(SEED)
    
    trainLoader = DataLoader( # help load the train data
        dataset=trainReader,
        batch_size=BATCH_SIZE,
        shuffle=True,
        drop_last=False,
        collate_fn=collate_fn,
        worker_init_fn=seed_worker,
        generator=g,
    )
    testLoader = DataLoader( # to help load the test data
        dataset = testReader,
        batch_size=BATCH_SIZE,
        shuffle=False,drop_last=False,collate_fn=collate_fn)
    optimizer = optim.Adam(predictor.parameters(), lr=LEARNING_RATE)
    
    EPOCHS = 20
    criterion = torch.nn.CrossEntropyLoss(reduction='mean')
    to_calc = {'loss':'mean','correct_count':'total'}
    #criterion = torch.nn.BCELoss(reduction='mean')
    
    #train_losses_all = []
    #train_aggregate_eval = []
    #test_losses_all = []
    #test_aggregate_eval = []
    losses_all = []
    aggregate_values = []
    no_need_print = ['sample_count','loop_type','epoch_no']
    
    for i in range(EPOCHS):
        print('Training Epoch',i)
        train_losses,last_pred,last_label_tensor,combo_count = do_one_epoch(predictor,trainLoader,True,criterion,optimizer,device=device)
        losses_all.extend(train_losses)
        #train_losses_all.extend(train_losses)
        train_overall_means = calculate_overall_means(train_losses,"sample_count",to_calc)
        precision_recall = precision_recall_from_combo(combo_count,len(HAVE_LABEL),False)
        for key in precision_recall:
            train_overall_means[key] = precision_recall[key]
        train_overall_means['loop_type'] = 'train'
        train_overall_means['epoch_no'] = i
        aggregate_values.append(train_overall_means)
        for key in train_overall_means:
            if key not in no_need_print:
                print(key+':',train_overall_means[key])
            if type(train_overall_means[key]) == list:
                train_overall_means[key] = ','.join([str(a) for a in train_overall_means[key]])
        #print(train_overall_means)
        print()
        
        test_losses,last_pred,last_label_tensor,combo_count = do_one_epoch(predictor,testLoader,False,criterion,None,device=device)
        losses_all.extend(test_losses)
        test_overall_means = calculate_overall_means(test_losses,"sample_count",to_calc)
        precision_recall = precision_recall_from_combo(combo_count,len(HAVE_LABEL),False)
        for key in precision_recall:
            test_overall_means[key] = precision_recall[key]
        test_overall_means['loop_type'] = 'test'
        test_overall_means['epoch_no'] = i
        aggregate_values.append(test_overall_means)
        for key in test_overall_means:
            if key not in no_need_print:
                print(key+':',test_overall_means[key])
            if type(test_overall_means[key]) == list:
                test_overall_means[key] = ','.join([str(a) for a in test_overall_means[key]])
        #print(test_overall_means)
        
        print()
        
        pd.DataFrame(losses_all).to_csv('model_losses.csv',index=False)
        pd.DataFrame(aggregate_values).to_csv('aggregate_results.csv',index=False)
        status_dict = {}
        status_dict['model_weights'] = predictor.state_dict()
        
        status_dict['image_size'] = IMAGE_SIZE
        status_dict['data_labels'] = HAVE_LABEL
        status_dict['frame_type'] = FRAME_TYPE
        status_dict['on_square'] = ON_SQUARE
        status_dict['x_pos'] = X_POS
        status_dict['y_pos'] = Y_POS
        
        epoch_data_small = open('epoch_'+str(i)+'_small.pkl','wb')
        pickle.dump(status_dict,epoch_data_small,protocol=pickle.HIGHEST_PROTOCOL)
        epoch_data_small.close()
        
        status_dict['optimizer_state'] = optimizer.state_dict()
        
        epoch_data = open('epoch_'+str(i)+'.pkl','wb')
        pickle.dump(status_dict,epoch_data,protocol=pickle.HIGHEST_PROTOCOL)
        epoch_data.close()
        
    
    
        
        
        
