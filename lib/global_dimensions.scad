include <../lib/global_variables.scad>

// Chassis
// The 4 corner points of the bottom plate
bl_vertex = [0,0];
tl_vertex = [0,body_length];
tr_vertex = [body_width,body_length];
br_vertex = [body_width,0];
// The 4 edges of the plate
b_edge = [br_vertex, bl_vertex];
l_edge = [bl_vertex, tl_vertex];
t_edge = [tl_vertex, tr_vertex];
r_edge = [tr_vertex, br_vertex];
