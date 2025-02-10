# -*- coding: utf-8 -*-
"""
Created on Mon Jan 20 13:00:48 2025

@author: Marquan
"""

import pandas as pd
import numpy as np
from PIL import Image
from enum import Enum
import torch

# class syntax

class Position(Enum):
    START = 0
    MIDDLE = 1
    END = 2


def get_position_start(original_size,new_size,position):
    if original_size == new_size:
        return 0
    elif position == Position.START:
        return 0
    elif position == Position.END:
        return new_size - original_size
    elif position == Position.MIDDLE:
        return int((new_size - original_size) / 2)


def image_on_square(pilframe,x_pos = Position.START,y_pos=Position.START):
    mode = pilframe.mode
    img_width,img_height = pilframe.size
    img_np = np.array(pilframe)
    max_len = max(img_width,img_height)
    img_shape = list(img_np.shape)
    img_shape[0] = max_len
    img_shape[1] = max_len
    new_np = np.ndarray(img_shape,dtype=img_np.dtype)
    new_np[:] = 0
    #x_start = 0
    x_start = get_position_start(img_width,max_len,x_pos)
    #y_start = 0
    y_start = get_position_start(img_height,max_len,y_pos)
    new_np[y_start:y_start+img_height,x_start:x_start+img_width] = img_np
    new_pil = Image.fromarray(new_np,mode)
    #new_np_back = np.array(pilframe)
    return(new_pil)


class ImageReader():
    def __init__(self,label_dir,have_label,image_directory="",which_dataset="train",transform=None,
                 to_reverse=False,frame_type="RGB",on_square=False,x_pos=Position.START,
                 y_pos=Position.START):
        self.feature_and_labels = []
        df = pd.read_csv(label_dir)
        self.frame_type = frame_type
        self.on_square = on_square
        self.transform = transform # transform is a function/ class to process the image
        self.have_label = have_label
        self.x_pos = x_pos
        self.y_pos = y_pos
        for i in range(len(df)):
            dataset = df.loc[i,"Dataset"]
            if dataset == which_dataset:
                file_name = df.loc[i,"File Name"]
                label = []
                for each in self.have_label:
                    if df.loc[i,"Have "+each]:
                        label.append(1)
                    else:
                        label.append(0)
                full_dir = file_name
                if image_directory != "":
                    full_dir = image_directory + "/" + full_dir
                if to_reverse: # if true, it will also return reversed copies of the image to help double the amount of data
                    self.feature_and_labels.append((full_dir,label,True))
                self.feature_and_labels.append((full_dir,label,False))
                #print(self.feature_and_labels[-1])

    def add_image(self,abs_dir,label,to_reverse=False):
        label_onehot = []
        for each in self.have_label:
            if each in label:
                label_onehot.append(1)
            else:
                label_onehot.append(0)
        if to_reverse:
            self.feature_and_labels.append((abs_dir,label_onehot,True))
        self.feature_and_labels.append((abs_dir,label_onehot,False))

    def convert_palette(self,image):
        if type(self.frame_type) == str:
            return image.convert(self.frame_type)
        elif len(self.frame_type) == 1:
            return image.convert(self.frame_type[0])
        else:
            return [image.convert(f_t) for f_t in self.frame_type]
    
    def __len__(self): # __len__ is the function that is ran whenever the len(arg) function is applied to the object
        return len(self.feature_and_labels)

    def __getitem__(self,idx): # __getitem__ is the function that returns the value when object[idx] is called
        file_name,label,to_rev = self.feature_and_labels[idx]
        image = Image.open(file_name)
        true_no = 0
        true_n = -1
        for i in range(len(label)):
            if label[i]:
                true_no += 1
                true_n = i
        if true_no != 1:
            true_n = len(label)
        label = true_n
        """
        if to_rev:
            image = image.transpose(Image.FLIP_LEFT_RIGHT)

        if self.on_square:
            image = image_on_square(image,self.x_pos,self.y_pos)

        image = self.convert_palette(image)
        
        """
        image = make_standard_image(image,to_rev,self.on_square,self.x_pos,self.y_pos,self.frame_type,None)
        
        if self.transform:
            image, label = self.transform(image, label)
        
        return image,label


def make_standard_image(image,to_rev=False,on_square=True,x_pos=Position.START,y_pos=Position.START,frame_type="RGB",transform=None,label=[]):
    # to reverse
    if to_rev:
        image = image.transpose(Image.FLIP_LEFT_RIGHT)
    
    # on square
    if on_square:
        image = image_on_square(image,x_pos,y_pos)
    
    # convert palette
    if type(frame_type) == str:
        image = image.convert(frame_type)
    elif len(frame_type) == 1:
        image = image.convert(frame_type[0])
    else:
        image = [image.convert(f_t) for f_t in frame_type]
    
    # transform
    if transform:
        image,label = transform(image,label)
    
    return image


# for combining transformation functions
class Compose(object):
    def __init__(self, transforms):
        self.transforms = transforms

    def __call__(self, img, labels):
        for t in self.transforms:
            if type(img) == list:
                img = [t(i) for i in img]
            else:
                img = t(img)
        if type(img) == list:
            img = torch.concat(img,dim=0)
        return img, labels