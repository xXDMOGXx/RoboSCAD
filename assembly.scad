// NOTICE! You must use preview on this script if you want to see your models

// Make sure that all parts you want included in the assembly are imported
include <global_variables.scad>
use <chassis.scad>

render = false;

// The initial rotation, translation, and distance of the camera. You can change this to whatever you like most
$vpr = [35,0,0];
$vpt = [body_width/2, 0, wall_height*2];
$vpd = 400;
// These two variables increase the resolution. Set render to false when editing the
// the model to reduce strain on your computer
$fa = render ? 1 : 12;
$fs = render ? 0.4 : 0.75;


// This is where you call all the parts you want loaded.
// If you give your parts parameters, you can call multiple of the same part with different outcomes.
// You can also directly apply transformations like translate and rotate here. I don't suggest it though
module load_objects() {
    chassis();
}

// Begins the loading of objects
load_objects();
