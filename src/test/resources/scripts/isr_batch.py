# Note: first need to install the dependency project.
# python3 ../image-super-resolution/setup.py install

# import statements
from PIL import Image
import numpy as np
from ISR.models import RDN
import sys
import os
import getopt
import time

workingdirectory = os.getcwd()

# working directory
print('working dir:', workingdirectory)

# Arguments
def parameters(argv):
    arg_help = "{0} -i <input> -o <output>".format(argv[0])
    params = dict();

    try:
        opts, args = getopt.getopt(argv[1:], "", ["help", "input1=", "output1=",  "input2=", "output2=", "input3=", "output3=", "input4=", "output4=", "input5=", "output5="])
    except:
        print(arg_help)
        sys.exit(2)

    if len(opts) == 0:
        print(arg_help)
    else:
        for opt, arg in opts:
            if opt in ("-h", "--help"):
                print(arg_help)  # print the help message
                sys.exit(2)
            elif opt in ("-i1", "--input1"):
                params['arg_input1'] = arg
            elif opt in ("-i2", "--input2"):
                params['arg_input2'] = arg
            elif opt in ("-i3", "--input3"):
                params['arg_input3'] = arg
            elif opt in ("-i4", "--input4"):
                params['arg_input4'] = arg
            elif opt in ("-i5", "--input5"):
                params['arg_input5'] = arg
            elif opt in ("-o1", "--output1"):
                params[ 'arg_output1'] = arg
            elif opt in ("-o2", "--output2"):
                params[ 'arg_output2'] = arg
            elif opt in ("-o3", "--output3"):
                params[ 'arg_output3'] = arg
            elif opt in ("-o4", "--output4"):
                params[ 'arg_output4'] = arg
            elif opt in ("-o5", "--output5"):
                params[ 'arg_output5'] = arg
        print('args:')
        for elem in map(str,params):
            print(elem+":"+params[elem], sep="\n")

    return params
def batch(inputFile, outputFile, model):
    # Read the image from the file to an array
    smallImage = np.array(Image.open(inputFile))

    # Enhance the image
    bigImage = model.predict(smallImage)

    # Write the image to the output file.
    Image.fromarray(bigImage).save(outputFile)
    return 0
def main(params):
    # Load the prediction model. Can be initialized once and then reused?
    start = time.time()
    model = RDN(weights='noise-cancel')
    end = time.time()

    # elapsed time
    print ('loading model elapsed time: ',end - start)
    # Declare the input and output file locations
    inputFile = r'{0}'.format(params['arg_input1']).strip()
    outputFile = r'{0}'.format(params['arg_output1']).strip()
    batch(inputFile, outputFile, model)
    inputFile = r'{0}'.format(params['arg_input2']).strip()
    outputFile = r'{0}'.format(params['arg_output2']).strip()
    batch(inputFile, outputFile, model)
    inputFile = r'{0}'.format(params['arg_input3']).strip()
    outputFile = r'{0}'.format(params['arg_output3']).strip()
    batch(inputFile, outputFile, model)
    inputFile = r'{0}'.format(params['arg_input4']).strip()
    outputFile = r'{0}'.format(params['arg_output4']).strip()
    batch(inputFile, outputFile, model)
    inputFile = r'{0}'.format(params['arg_input5']).strip()
    outputFile = r'{0}'.format(params['arg_output5']).strip()
    batch(inputFile, outputFile, model)
if __name__ == "__main__":
    main(parameters(sys.argv))
