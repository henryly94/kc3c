#!/bin/python

import os

content ='''
---
layout: page
title: 实验报告
permalink: /report/
---
'''
try:
    index_file = open('index.md')


except Exception as e:
    raise

finally:

    index_file.close()

print(content)

# os.system(content+  " > test")
