// Helper function. Finds the point that the two edges intersect, and turns the
// other two points into vectors based off of the corner position
function sort_edge_points(edge1, edge2) =
    edge1[0] == edge2[0] ?
        [edge1[0],edge1[1] - edge1[0],edge2[1] - edge1[0]]
    : edge1[0] == edge2[1] ?
        [edge1[0],edge1[1] - edge1[0],edge2[0] - edge1[0]]
    : edge1[1] == edge2[0] ?
        [edge1[1],edge1[0] - edge1[1],edge2[1] - edge1[1]]
    : 
        [edge1[1],edge1[0] - edge1[1],edge2[0] - edge1[1]];

// Finds the magnitude of a component vector
function v_mag(v) = sqrt((v[0]^2)+(v[1]^2));
// Finds the direction (angle) of a component vector
function v_dir(v) = atan2(v[1],v[0]);
// Finds x vector component from magnitude and direction
function vx(m, d) = m*cos(d);
// Finds y vector component from magnitude and direction
function vy(m, d) = m*sin(d);
// Finds the dot product of two vectors
function dot_p(a, b) = a[0]*b[0]+a[1]*b[1];
// Finds the angle between two intersecting vectors
function find_angle(a, b) = acos(dot_p(a,b)/(v_mag(a)*v_mag(b)));
// Finds the closest rotation to get from a1 to a2. -180 to 180
function angle_distance(a1, a2) = (((a2-a1+180)%360-360)%360+180);

// Extrude blind stretches a 2D sketch by a certain amount to create a 3D shape
// int direction: Determines which way the extrude goes
// float amount: Determines how far to extrude
module extrude_blind(direction, amount) {
    // Any positive number extrudes in the positive direction
    if (direction > 0) {
        linear_extrude(amount)
        children();
    // Any positive number extrudes in the negative direction
    } else if (direction < 0) {
        translate([0, 0, -amount])
        linear_extrude(amount)
        children();
    // 0 extrudes in both directions
    } else {
        translate([0, 0, -amount])
        linear_extrude(amount*2)
        children();
    }
}

// Coincident origin moves the origin of a part to a specific coordinate
// float[3] translation: The coordinate position to move the part's origin to
// float[3] offset: The coordinate offset from the translation. Defaults to no offset
module coincident_origin(translation, o=[0,0,0]) {
    // Applies the translation and offset to all children
    translate(translation+o)
    children();
}

// Sketch plane defines the plane of rotation and translation that a sketch is on
// float plane[2][3]: An array that holds a rotation and translation array
module sketch_plane(plane) {
    rotation = plane[0];
    translation = plane[1];
    // Applies the translation and rotation to all children
    rotate(rotation)
    translate(translation)
    children();
}

// Outer Fillet 2D applies a fillet to an outer corner of two 2D intersecting lines
// float radius: The radius of the circle used to fillet the corner
// float[2][2] edge1: Array of two 2D points ([x,y]). Order of points or edges doesn't matter
// float[2][2] edge2: Array of two 2D points ([x,y]). Order of points or edges doesn't matter
// Both edges must be a line that terminates at the mutual intersection
module outer_fillet_2D(radius, edge1, edge2) {
    // Sorts the edges to find the vertex and the position vectors
    sorted = sort_edge_points(edge1, edge2);
    corner = sorted[0];
    v1 = sorted[1];
    v2 = sorted[2];
    // Finds the angle direction of both position vectors
    v1d = v_dir(v1);
    v2d = v_dir(v2);
    // Finds the angle between the two vectors
    direction = find_angle(v1, v2) / 2;
    // Finds which angle is the initial side of the angle (the other would be the terminal)
    initial = angle_distance(v1d, v2d) > 0 ? v1d : v2d;
    // Offsets the initial direction to the middle of both vectors
    o_dir = initial + direction;
    // Finds the distance from the vertex to the center of a circle tangent to both lines
    s = radius / (sin(direction));
    // Finds the coordinate position of s
    center = [corner[0]+vx(s, o_dir), corner[1]+vy(s, o_dir)];
    // Finds the distance from the vertex to either tangent point on the circle
    tan_l = sqrt((s^2)-(radius^2));
    // Finds the coordinate position of both tangent points
    tan1 = [corner[0]+vx(tan_l, v1d), corner[1]+vy(tan_l, v1d)];
    tan2 = [corner[0]+vx(tan_l, v2d), corner[1]+vy(tan_l, v2d)];
    
    // Cuts out the final polygon from all the children, leaving behind a perfect filleted corner
    difference() {
        children();
        // Cuts the circle out of the polygon, leaving behind everything the fillet removes
        difference() {
            // creates a polygon that follows the form of the edges and center point
            polygon(points = [corner, tan1, center, tan2]);
            // Moves the circle to be tangent to both edges
            translate(center) {
                // Creates a circle with the radius of the fillet
                circle(radius);
            }
        }
    }
}