// These three imports are added to every part file you make
include <../lib/global_variables.scad>
include <../lib/global_dimensions.scad>
use <../lib/commands.scad>

// Each sketch in a part will have it's own module
module bottom_plate_sketch() {
    // All the points are defined in global_dimensions

    // Creates a fillet on all 4 corners with radius of wall_outer_corner_fillet
    fillet_2D(wall_outer_corner_fillet, b_edge, l_edge, true)
    fillet_2D(wall_outer_corner_fillet, l_edge, t_edge, true)
    fillet_2D(wall_outer_corner_fillet, t_edge, r_edge, true)
    fillet_2D(wall_outer_corner_fillet, r_edge, b_edge, true)
    // The initial shape of the sketch goes at the very bottom.
    // Transformations are applied from the bottom up
    polygon(points = [bl_vertex, tl_vertex, tr_vertex, br_vertex]);
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

if (export) {
    // These two variables increase the resolution. Set render to false when editing the
    // the model to reduce strain on your computer
    $fa = render ? 1 : 12;
    $fs = render ? 0.4 : 0.75;
    chassis();
}