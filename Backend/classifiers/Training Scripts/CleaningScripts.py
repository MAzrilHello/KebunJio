# -*- coding: utf-8 -*-
"""
Created on Tue Jan 21 07:26:14 2025

@author: TheMarksman
"""

def combine_str_arr(str_arr):
    if type(str_arr) == str:
        return str_arr
    elif len(str_arr) == 1:
        return str_arr[0]
    else:
        return ','.join(str_arr)