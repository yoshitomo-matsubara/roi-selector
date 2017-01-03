# Sample Set maker
My private Maven project for GUI-based program to visually select region of interest (ROI) in training samples for OpenCV 2.x.  
(Now you can use *opencv_annotation* in OpenCV 3.x instead.)

## Requirement
Java 1.7+  

## Usage
    $ java -cp sample-set-maker.jar ymatsubara.roiselector.gui.MainFrame

## Structure
- **roi-selector.jar**
- **PositiveImg/**: automatically created directory for positive image files to select ROI
- **NegativeImg/**: automatically created directory for negative image files
- **positive.dat**: output positive ROI file for OpenCV training (output when you save [Ctrl + s])
- **negative.dat**: output negative file for OpenCV training (output when you save [Ctrl + s])