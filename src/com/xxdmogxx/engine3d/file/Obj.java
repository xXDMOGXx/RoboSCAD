package com.xxdmogxx.engine3d.file;

import com.xxdmogxx.engine3d.datatypes.Loc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Obj {
    public static ArrayList<Loc[]> loadObjFile(String file_name) {
        List<Loc> vlist = new ArrayList<>();
        ArrayList<Loc[]> tlist = new ArrayList<>();
        File objFile = new File(file_name);
        Scanner fileReader;
        try {
            fileReader = new Scanner(objFile);
        } catch (FileNotFoundException e) {
            System.out.println("File " + file_name + " does not exist");
            return new ArrayList<>();
        }
        while (fileReader.hasNextLine()) {
            String data = fileReader.nextLine();
            String desc = data.substring(0, 1);
            String line = data.substring(2);
            if (desc.equals("v")) {
                String[] vs = line.split(" ");
                Loc vertex = new Loc(Float.parseFloat(vs[0]),
                        Float.parseFloat(vs[1]), Float.parseFloat(vs[2]));
                vlist.add(vertex);
            } else if (desc.equals("f")) {
                String[] ts = line.split(" ");
                int[] faces = {Integer.parseInt(ts[0]), Integer.parseInt(ts[1]), Integer.parseInt(ts[2])};
                Loc[] triangle = {vlist.get(faces[0]-1), vlist.get(faces[1]-1), vlist.get(faces[2]-1)};
                tlist.add(triangle);
            }
        }
        fileReader.close();
        return tlist;
    }
}
