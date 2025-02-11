# -*- coding: utf-8 -*-
"""
Created on Tue Jan 21 16:28:30 2025

@author: TheMarksman
"""

import torch
import numpy as np
import random
from tqdm import tqdm


def seed_worker(some_var):
    np.random.seed(some_var)
    random.seed(some_var)


def do_one_epoch(model,loader,is_train,criterion,optimizer=None,epoch_no=0,device='cpu'):
    if is_train:
        model.train()
        loop_type = 'train'
    else:
        model.eval()
        loop_type = 'test'
    loop = tqdm(loader, leave=True) # tqdm is a class that enables a 'loading bar'
    epoch_data = []
    sub_epoch = -1
    combo_count = {}
    for batch_idx, (features,labels) in enumerate(loop):
        sub_epoch += 1
        sample_count = len(features)
        # reset the optimizer
        if is_train:
            optimizer.zero_grad() # reset optimiser
        #print('device:',device)
        features = features.to(device)
        #print(type(features))
        if is_train:
            pred = model(features) # predict probabilities
        else:
            with torch.no_grad():
                pred = model(features)
        C = len(pred[0])
        pred_label = torch.topk(pred,1).indices.squeeze(1)
        label_tensor = torch.Tensor(labels).long().to(device)
        combo_count = pred_actual_combo_count(pred_label,label_tensor,C,combo_count)
        correct_count = torch.sum(label_tensor == pred_label).item()
        loss = criterion(pred,label_tensor) # calculate loss function
        loop.set_postfix(sample_count=sample_count,loss=loss.item(),accuracy=correct_count/sample_count)
        if is_train:
            loss.backward()
            optimizer.step()
        else:
            pass
        epoch_data.append({'epoch_no':epoch_no,'loop_type':loop_type,'sub_epoch':sub_epoch,'sample_count':sample_count,'loss':loss.item(),'correct_count':correct_count})
        #if sub_epoch == 12:
        #    break
        #print(pred)
        #print(label_tensor)
        #break
    return epoch_data,pred,label_tensor,combo_count



def do_one_epoch_single(model,loader,is_train,criterion,optimizer=None,epoch_no=0,device='cpu',sigmoid_pred=False):
    if is_train:
        model.train()
        loop_type = 'train'
    else:
        model.eval()
        loop_type = 'test'
    loop = tqdm(loader, leave=True) # tqdm is a class that enables a 'loading bar'
    epoch_data = []
    sub_epoch = -1
    combo_count = {(1,0):0,(1,1):0,(0,0):0,(0,1):0}
    for batch_idx, (features,labels) in enumerate(loop):
        sub_epoch += 1
        sample_count = len(features)
        # reset the optimizer
        if is_train:
            optimizer.zero_grad() # reset optimiser
        #print('device:',device)
        features = features.to(device)
        #print(type(features))
        if is_train:
            pred = model(features) # predict probabilities
        else:
            with torch.no_grad():
                pred = model(features)
        if sigmoid_pred:
            pred = torch.sigmoid(pred)
        if len(pred.shape) > 1:
            pred = pred.squeeze(1)
        #print(pred)
        #C = len(pred[0])
        pred_label = (pred > 0).long()
        label_tensor = torch.Tensor(labels).long().to(device).float()
        #print(pred_label)
        #print(label_tensor)
        for p_i in range(2):
            for l_i in range(2):
                #print(p_i,l_i)
                #print(len(pred_label[(pred_label==p_i) & (label_tensor==l_i)]))
                combo_count[(p_i,l_i)] += len(pred_label[(pred_label==p_i) & (label_tensor==l_i)])
        #print(combo_count)
        correct_count = torch.sum(label_tensor == pred_label).item()
        #print(correct_count)
        #print(pred)
        #print(label_tensor)
        loss = criterion(pred,label_tensor) # calculate loss function
        loop.set_postfix(sample_count=sample_count,loss=loss.item(),accuracy=correct_count/sample_count)
        if is_train:
            loss.backward()
            optimizer.step()
        else:
            pass
        epoch_data.append({'epoch_no':epoch_no,'loop_type':loop_type,'sub_epoch':sub_epoch,'sample_count':sample_count,'loss':loss.item(),'correct_count':correct_count})
        #if sub_epoch == 12:
        #    break
        #print(pred)
        #print(label_tensor)
        #break
    return epoch_data,pred,label_tensor,combo_count



def calculate_overall_means(value_list,sample_count_key,to_calc):
    final_calc = {}
    for key in to_calc:
        final_calc[key] = 0
    number_of_samples = 0
    for each in value_list:
        sample_count = each[sample_count_key]
        number_of_samples += sample_count
        for key in to_calc:
            if to_calc[key] == 'mean':
                subtotal = each[key]*sample_count
            else:
                subtotal = each[key]
            final_calc[key] += subtotal
    for key in final_calc:
        final_calc[key] = final_calc[key] / number_of_samples
    final_calc[sample_count_key] = number_of_samples
    return final_calc


def collate_fn(batch): # function to combine multiple image-label pairs into image tensors and label tensors
    R = len(batch)
    img_list = []
    label_list = []
    for r in range(R):
        img_list.append(batch[r][0].unsqueeze(0))
        label_list.append(batch[r][1])
    img_list = torch.cat(img_list)
    label_list = np.array(label_list)
    return(img_list,label_list)


def pred_actual_combo_count(pred,actual,class_count,prev_count=None):
    if prev_count == None:
        prev_count = {} 
    for cp in range(class_count):
        for ca in range(class_count):
            key = (cp,ca)
            if key not in prev_count:
                prev_count[key] = 0
            prev_count[key] += len(pred[(pred==cp) & (actual==ca)])
    return prev_count



def precision_recall_from_combo_single(combo_count_dict):
    pt_at = combo_count_dict[(1,1)]
    pt_af = combo_count_dict[(1,0)]
    pf_at = combo_count_dict[(0,1)]
    pf_af = combo_count_dict[(0,0)]
    total = pt_at + pt_af + pf_at + pf_af
    accuracy = (pt_at + pf_af) / total
    if pt_at + pt_af > 0:
        precision = pt_at / (pt_at + pt_af)
    else:
        precision = None
    if pt_at + pf_at > 0:
        recall = pt_at / (pt_at + pf_at)
    else:
        recall = None
    return_dict = {}
    return_dict['true_positive'] = pt_at
    return_dict['false_positive'] = pt_af
    return_dict['false_negative'] = pf_at
    return_dict['true_negative'] = pf_af
    return_dict['precision'] = precision
    return_dict['recall'] = recall
    return return_dict


def precision_recall_from_combo(combo_count_dict,class_count,exclude_zero_denom=True):
    
    total_true_pos = 0
    
    micro_prec_denom = 0 # TP + FP
    
    micro_recall_denom = 0 # TP + FN
    
    total_precision = 0
    prec_nozero = 0
    prec_zero = []
    total_recall = 0
    recall_nozero = 0
    recall_zero = []
    
    T = 0
    for key in combo_count_dict:
        T += combo_count_dict[key]
    
    for c in range(class_count):
        pt_at = combo_count_dict[(c,c)] # true positive
        pt_af = sum([combo_count_dict[(c,ci)] for ci in range(class_count) if ci != c]) # false positive
        pf_at = sum([combo_count_dict[(ci,c)] for ci in range(class_count) if ci != c]) # false negative
        #pf_af = T - pt_at - pt_af - pf_at # true negative
        #print("*** class",c,"***")
        #print(pt_at,pt_af,pf_at,pf_af)
        
        total_true_pos = total_true_pos + pt_at
        micro_prec_denom = micro_prec_denom + pt_at + pt_af
        micro_recall_denom = micro_recall_denom + pt_at + pf_at
        #print(total_true_pos,micro_prec_denom,micro_recall_denom)
        if pt_at + pt_af != 0:
            total_precision += pt_at / (pt_at + pt_af)
            prec_nozero += 1
        else:
            prec_zero.append(c)
        if pt_at + pf_at != 0:
            total_recall += pt_at / (pt_at + pf_at)
            recall_nozero += 1
        else:
            recall_zero.append(c)
        #pt_af = len(pred[(pred==c) & (actual!=c)]) # false positive
        #pf_at = len(pred[(pred!=c) & (actual==c)]) # false negative
        #pf_af = len(pred[(pred!=c) & (actual!=c)]) # true negative
    #print(total_true_pos,micro_prec_denom,micro_recall_denom)
    micro_precision = total_true_pos / micro_prec_denom
    micro_recall = total_true_pos / micro_recall_denom
    macro_precision = total_precision / (prec_nozero if exclude_zero_denom else class_count)
    macro_recall = total_recall / (recall_nozero if exclude_zero_denom else class_count)
    return({'micro_precision':micro_precision,
            'micro_recall':micro_recall,
            'macro_precision':macro_precision,
            'prec_nozero':prec_nozero,
            'prec_zero':prec_zero,
            'macro_recall':macro_recall,
            'recall_nozero':recall_nozero,
            'recall_zero':recall_zero})
    

if __name__ == '__main__':
    combo_count_dict = {(0,0):7,(0,1):3,(1,0):2,(1,1):8}
    print(precision_recall_from_combo(combo_count_dict,2))
    