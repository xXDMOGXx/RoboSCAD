// NOTICE! You must use preview on this script if you want to see your models

// Make sure that all parts you want included in the assembly are imported
use <parts/chassis.scad>

// These two variables increase the resolution. Keep them commented when editing the
// the model to reduce strain on your computer
//$fa = 1;
//$fs = 0.4;

// This is where you call all the parts you want loaded.
// If you give your parts parameters, you can call multiple of the same part with different outcomes.
// You can also directly apply transformations like translate and rotate here. I don't suggest it though
module load_objects() {
    chassis();
}

// Begins the loading of objects
load_objects();
