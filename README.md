# RoboSCAD

To view the .scad models, you must have OpenSCAD downloaded. I suggest also using VSCode with the OpenSCAD plugin (not OpenSCAD Language Support) to edit the scripts.  
Make sure all of the files are in the same folder, or else they won't properly import each other.  

global_variables.scad holds the variables that are available across all of your parts.  
commands.scad holds all of the custom modules that RoboSCAD offers.  
assembly.scad is the main script you run. This is where you initialize all the parts you want to see in the preview.  
Any other files (like chassis.scad) are part files. These are where you create individual parts in.  
