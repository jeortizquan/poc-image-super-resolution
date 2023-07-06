import numpy as np
import sys
import time
import logging
import io

from flask import Flask, request, jsonify, send_file
from waitress import serve
from error_handlers import errors
from PIL import Image
from ISR.models import RDN, RRDN

if __name__=="__main__":
    app = Flask(__name__)
    app.register_blueprint(errors)
    host = '0.0.0.0'
    port = 5050
    app.logger.setLevel(logging.INFO)

    # Load the prediction model. Can be initialized once and then reused?
    start = time.time()
    model = RDN(weights='noise-cancel')
    end = time.time()

    # modal elapsed time
    app.logger.info('loading model elapsed time: %ds', end - start)

    @app.route("/")
    def index():
        """Return JSON with welcome message."""
        response = {
            "code": 200,
            "name": "GET",
            "description": "Welcome to image super resolution enhancement"
        }
        return jsonify(response)

    @app.route('/enhance/file', methods=['POST','GET'])
    def enhance_file():
        """Return JSON message of success, and the image is stored on the indicated output."""
        if request.method == 'POST':
            img = Image.open(request.json['input'])
            sr_img = model.predict(np.array(img))
            sr_img = Image.fromarray(sr_img)
            sr_img.save(request.json['output'])
            response = {
                "code": 200,
                "name": "POST",
                "description": 'successful'
            }
            return jsonify(response)

        if request.method == 'GET':
            response = {
                "code": 200,
                "name": "GET",
                "description": "Post here a low-resolution image file path"
            }
            return jsonify(response)

    @app.route('/enhance/image', methods=['POST','GET'])
    def enhance_image():
        """Return the content-type image/jpeg enhanced."""
        if request.method == 'POST':
            image_file = request.files['image']
            img = Image.open(image_file.stream)
            sr_img = model.predict(np.array(img))
            sr_img = Image.fromarray(sr_img)
            raw_bytes = io.BytesIO()
            sr_img.save(raw_bytes, "JPEG")
            raw_bytes.seek(0)

            return send_file(raw_bytes, mimetype="image/jpeg")

        if request.method == 'GET':
            response = {
                "code": 200,
                "name": "GET",
                "description": "Post here a low-resolution image in binary format"
            }
            return jsonify(response)

    @app.route('/enhance/batchfile', methods=['POST','GET'])
    def enhance_batch():
        """Return JSON message of success, and the images are stored on the indicated output."""
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
            response = {
                "code": 200,
                "name": "POST",
                "description": 'successful'
            }
            return jsonify(response)

        if request.method == 'GET':
            response = {
                "code": 200,
                "name": "GET",
                "description": "I am a model for enhancing low-resolution images in batches"
            }
            return jsonify(response)
    # serve the application
    serve(app, host=host, port=port)
