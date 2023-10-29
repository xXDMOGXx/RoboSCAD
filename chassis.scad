include <global_variables.scad>
use <commands.scad>

module bottom_plate() {
    BL = [0,0];
    TL = [0,body_length];
    TR = [body_width,body_length];
    BR = [body_width,0];

    outer_fillet_2D(wall_outer_corner_fillet, [BR,BL], [BL,TL])
    outer_fillet_2D(wall_outer_corner_fillet, [BL,TL], [TL,TR])
    outer_fillet_2D(wall_outer_corner_fillet, [TL,TR], [TR,BR])
    outer_fillet_2D(wall_outer_corner_fillet, [TR,BR], [BR,BL])
    polygon(points = [BL,TL,TR,BR]);
}

module chassis() {
    coincident_origin(global_origin)
    
    sketch_plane(flat_plane)
    extrude_blind(1, plate_thickness) {
        bottom_plate();
    }
}