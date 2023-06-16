# Note: first need to install the dependency project.
# python3 ../image-super-resolution/setup.py install

# import statements
from PIL import Image
import numpy as np
from ISR.models import RDN, RRDN
import sys
import os
import getopt

workingdirectory = os.getcwd()

# working directory
print('working dir:', workingdirectory)

def main():
    # Load the prediction model. Can be initialized once and then reused?
    model = {:2}

    # Declare the input and output file locations
    inputFile = r'{:0}'
    outputFile = r'{:1}'

    # Read the image from the file to an array
    smallImage = np.array(Image.open(inputFile))

    # Enhance the image
    bigImage = model.predict(smallImage)

    # Write the image to the output file.
    Image.fromarray(bigImage).save(outputFile)

    # success when this line is executed
    sys.exit(0)
if __name__ == "__main__":
    main()
