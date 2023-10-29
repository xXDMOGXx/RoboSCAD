// Make sure to add these two imports to every part file you make
include <global_variables.scad>
use <commands.scad>

// Each sketch in a part will have it's own module
module bottom_plate_sketch() {
    // Just some variable that hold the 4 corner points of the plate
    BL = [0,0];
    TL = [0,body_length];
    TR = [body_width,body_length];
    BR = [body_width,0];

    // Creates a fillet on all 4 corners with radius of wall_outer_corner_fillet
    outer_fillet_2D(wall_outer_corner_fillet, [BR,BL], [BL,TL])
    outer_fillet_2D(wall_outer_corner_fillet, [BL,TL], [TL,TR])
    outer_fillet_2D(wall_outer_corner_fillet, [TL,TR], [TR,BR])
    outer_fillet_2D(wall_outer_corner_fillet, [TR,BR], [BR,BL])
    // The initial shape of the sketch goes at the very bottom.
    // Transformations are applied from the bottom up
    polygon(points = [BL,TL,TR,BR]);
}

// This is your main part constructor. You will call this from the assembly
module chassis() {
    // All your mates go at the top
    coincident_origin(global_origin)
    
    // Everything within a union will become 1 single 3D model.
    // If you want to break a part into multiple models, you can
    // create multiple different unions
    union() {
        // Each sketch needs a sketch plane. This is the surface that the sketch is on.
        sketch_plane(flat_plane)
        // This is a command. You can apply many different commands to a sketch.
        // Extrude blind takes a direction (based on sign) and an amount
        extrude_blind(1, plate_thickness) {
            bottom_plate_sketch();
        }
    }
}