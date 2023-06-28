from PIL import Image
from pathlib import Path
import numpy as np
import time
from flask import Flask, request, jsonify

import sys
sys.path.append('image-super-resolution')
from ISR.models import RDN, RRDN

if __name__=="__main__":

    endpoint_name = 'srapi'
    endpoint_batch = 'srapibatch'
    port = 5050
    host = '0.0.0.0'

    # Load the prediction model. Can be initialized once and then reused?
    start = time.time()
    model = RDN(weights='noise-cancel')
    end = time.time()

    # elapsed time
    print ('loading model elapsed time: ',end - start)

    app = Flask(__name__)
    @app.route(f'/{endpoint_name}', methods=['POST','GET'])
    def predict():

        if request.method == 'POST':
            img = Image.open(request.json['input'])
            sr_img = model.predict(np.array(img))
            sr_img = Image.fromarray(sr_img)
            sr_img.save(request.json['output'])
            return jsonify({'message':'successful'})

        if request.method == 'GET':
            return jsonify({'message':"I am a model for enhancing low-resolution images"})

    @app.route(f'/{endpoint_batch}', methods=['POST','GET'])
    def predictb():

        if request.method == 'POST':
            img = Image.open(request.json['input1'])
            sr_img = model.predict(np.array(img))
            sr_img = Image.fromarray(sr_img)
            sr_img.save(request.json['output1'])

            img = Image.open(request.json['input2'])
            sr_img = model.predict(np.array(img))
            sr_img = Image.fromarray(sr_img)
            sr_img.save(request.json['output2'])

            img = Image.open(request.json['input3'])
            sr_img = model.predict(np.array(img))
            sr_img = Image.fromarray(sr_img)
            sr_img.save(request.json['output3'])

            img = Image.open(request.json['input4'])
            sr_img = model.predict(np.array(img))
            sr_img = Image.fromarray(sr_img)
            sr_img.save(request.json['output4'])

            img = Image.open(request.json['input5'])
            sr_img = model.predict(np.array(img))
            sr_img = Image.fromarray(sr_img)
            sr_img.save(request.json['output5'])

            return jsonify({'message':'successful'})

        if request.method == 'GET':
            return jsonify({'message':"I am a model for enhancing low-resolution images in batch"})
    app.run(host=host, port=port, threaded=True)
