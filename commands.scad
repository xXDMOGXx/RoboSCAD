overlap_insurance = 1;

function sort_edge_points(edge1, edge2) =
    edge1[0] == edge2[0] ?
        [edge1[0],edge1[1] - edge1[0],edge2[1] - edge1[0]]
    : edge1[0] == edge2[1] ?
        [edge1[0],edge1[1] - edge1[0],edge2[0] - edge1[0]]
    : edge1[1] == edge2[0] ?
        [edge1[1],edge1[0] - edge1[1],edge2[1] - edge1[1]]
    : 
        [edge1[1],edge1[0] - edge1[1],edge2[0] - edge1[1]];

function v_mag(v) = sqrt((v[0]^2)+(v[1]^2));
function v_dir(v) = atan2(v[1],v[0]);
function vx(m, d) = m*cos(d);
function vy(m, d) = m*sin(d);
function dot_p(a, b) = a[0]*b[0]+a[1]*b[1];
function find_angle(a, b) = acos(dot_p(a,b)/(v_mag(a)*v_mag(b)));
function angle_distance(a1, a2) = (((a1-a2+180)%360-360)%360+180);

// Direction 0 extrudes both directions
module extrude_blind(direction, amount) {
    if (direction > 0) {
        linear_extrude(amount)
        children();
    } else if (direction < 0) {
        translate([0, 0, -amount])
        linear_extrude(amount)
        children();
    } else {
        translate([0, 0, -amount])
        linear_extrude(amount*2)
        children();
    }
}

module coincident_origin(translation, o=[0,0,0]) {
    translate(translation+o)
    children();
}

module sketch_plane(plane) {
    rotation = plane[0];
    translation = plane[1];
    rotate(rotation)
    translate(translation)
    children();
}

module outer_fillet_2D(radius, edge1, edge2) {
    sorted = sort_edge_points(edge1, edge2);
    corner = sorted[0];
    v1 = sorted[1];
    v2 = sorted[2];
    v1d = v_dir(v1);
    v2d = v_dir(v2);
    direction = find_angle(v1, v2) / 2;
    initial = angle_distance(v2d, v1d) > 0 ? v1d : v2d;
    o_dir = initial + direction;
    s = radius / (sin(direction));
    center = [corner[0]+vx(s, o_dir), corner[1]+vy(s, o_dir)]; 
    tan_l = sqrt((s^2)-(radius^2));
    tan1 = [corner[0]+vx(tan_l, v1d), corner[1]+vy(tan_l, v1d)];
    tan2 = [corner[0]+vx(tan_l, v2d), corner[1]+vy(tan_l, v2d)];
    
    
    difference() {
        children();
        difference() {
            polygon(points = [corner, tan1, center, tan2]);
            translate(center) {
                circle(radius);
            }
        }
    }
}