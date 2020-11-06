# Developed with assitance of professor

import os
import random
import nltk
import numpy as math


def load_data(dir):
    list = []
    for file in os.listdir(dir):
        #print("hi!")
        with open(dir + '/' + file, 'rb') as f:
            body = f.read().decode('utf-8', errors='ignore').splitlines()
            list.append(' '.join(body))
    return list

BASE_DATA_DIR= '/Users/timothyswang/Documents/EE364Comp/ComputerProblem2/enron1'

# load and tag data
ham = [(text, 'ham') for text in load_data(BASE_DATA_DIR + '/ham')]
spam = [(text, 'spam') for text in load_data(BASE_DATA_DIR + '/spam')]

both = ham + spam

def preprocess(text):


    #Make everything lowercase
    text = text.lower()

    #Tokenize the words in the emails
    from nltk.tokenize import RegexpTokenizer
    # only keep alphabet words
    tokenizer = RegexpTokenizer(r'[a-z]+')
    # get list of list of tokens
    tokens = tokenizer.tokenize(text)

    #Remove suffixes such as "-ing" or "-ed" etc.
    from nltk.stem import WordNetLemmatizer
    lemmatizer = WordNetLemmatizer()
    # map list of tokens
    tokens = [lemmatizer.lemmatize(t) for t in tokens]

    #Remove "stopwords" such as "the" or "a" etc.
    from nltk.corpus import stopwords
    stoplist = stopwords.words('english')
    # filter list of list of tokens
    tokens = [t for t in tokens if not t in stoplist]

    # then return a list of tokens (words)
    return tokens

both = [(preprocess(text), label) for (text,label) in both]

# shuffle
random.shuffle(both)
# split train/test
splitp = 0.80 # 80/20 split
train = both[:int(splitp*len(both))]
test = both[int(splitp*len(both)):]

SpamDict = {}
HamDict = {}
totalSpamWords = 0.
totalNotSpamWords = 0.

#Calculate probability that a message is spam in the traning set
count1 = 0.
count2 = 0.
count5 = 0.

for (tokens,label) in train:
    count2 += 1.

    if label == 'spam':
        count1 += 1.
    else:
        count5 += 1.

    #check each word in the email
    for word in tokens:
        
        if label == 'spam':
            
            totalSpamWords = totalSpamWords + 1.
            if word in SpamDict:
                SpamDict[word] += 1.
            else:
                SpamDict[word] = 1.
        
        else:
            
            totalNotSpamWords += 1.
            if word in HamDict:
                HamDict[word] += 1.
            else:
                HamDict[word] = 1.
        
    #count into the correct dictionary (use an if statement)
        
P_Spam = count1/count2
P_NotSpam = count5/count2 #Probability that a message is not spam in the training set

#Go through the test emails

TruePos = 0. #Number of spam emails labeled spam
FalseNeg = 0. #Number of spam emails labeled not spam
FalsePos = 0. #Number of not spam emails labeled spam
TrueNeg = 0. #Number of not spam emails labeled not spam

count3 = 0. #Number of emails that's truly spam
count4 = 0. #Number of emails that's truly not spam

for (tokens, label) in test:
    calcP_Spam = 0.
    calcP_NotSpam = 0.
    for word in tokens:
        calcP_Spam = calcP_Spam + math.log((SpamDict.get(word,0.0) + 1)/(totalSpamWords + len(SpamDict) + 1))

        calcP_NotSpam = calcP_NotSpam + math.log((HamDict.get(word,0.0) + 1)/(totalNotSpamWords + len(HamDict) + 1))

    calcP_Spam = calcP_Spam + math.log(P_Spam) #Add the log of the P of Spam
    calcP_NotSpam = calcP_NotSpam + math.log(P_NotSpam)


    if label == 'spam': #If it's truly spam
        count3 = count3 + 1.
        if calcP_Spam > calcP_NotSpam: #labeled as Spam
            TruePos += 1.
        else: #labeled as not Spam
            FalseNeg += 1.
    else: #If it's truly not spam
        count4 = count4 + 1.
        if calcP_Spam > calcP_NotSpam: #labeled as Spam
            FalsePos += 1.
        else: #labeled as Not Spam
            TrueNeg += 1.

#Get the rates
#TruePos, False Neg --> Divide by total spam emails
#False pos, true neg --> Divide by total not spam emails

TruePosRate = TruePos/count3
FalseNegRate = FalseNeg/count3

FalsePosRate = FalsePos/count4
TrueNegRate = TrueNeg/count4

import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sn
df = pd.DataFrame([[TruePosRate, FalsePosRate], [FalseNegRate,
    TrueNegRate]])
fig = plt.figure()
ax = sn.heatmap(100*df, vmin=0, vmax=100, cmap='Blues',
    annot=True, fmt='.2f', annot_kws={"size":16}, linewidths=0.5)
ax.set_xlabel('Truth')
ax.set_ylabel('Prediction')
ax.set_xticklabels(['spam', '˜spam'])
ax.set_yticklabels(['spam', '˜spam'])
plt.show()
"""


