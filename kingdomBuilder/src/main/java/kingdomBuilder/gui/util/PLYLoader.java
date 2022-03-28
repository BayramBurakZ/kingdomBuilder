package kingdomBuilder.gui.util;

import javafx.scene.shape.TriangleMesh;

import java.io.*;
import java.net.URI;

/**
 * Class that is used to load a 3D-model from a .ply-file.
 *
 * Important: triangulate must be on in blender export!
 */
public class PLYLoader {

    /**
     * Loads a 3D Model from a .ply-File into a TriangleMesh.
     * @param uri the Location of the .ply-File.
     * @return The 3D Model or null if something goes wrong.
     */
    public static TriangleMesh readFromPlyFile(URI uri) {
        // Initialize
        TriangleMesh mesh = new TriangleMesh();

        int vertexCount = 0;
        int faceCount = 0;

        float[] points = new float[0];
        float[] texCoords = new float[0];
        int[] faces = new int[0];


        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(uri)));

            String line = bufferedReader.readLine();

            // reads the header
            while (line != null) {
                String[] words = line.split(" ");

                // if end header, break out of reading the header
                if (words[0].equals("end_header")) break;

                if(words[0].equals("element")) {
                    if(words[1].equals("vertex"))
                    {
                        // Number of Vertex
                        // set length of arrays
                        vertexCount = Integer.parseInt(words[2]);
                        points = new float[vertexCount * 3];
                        texCoords = new float[vertexCount * 2];
                    }
                    if(words[1].equals("face")) {
                        // number of Faces in Javafx way of formatting it
                        faceCount = Integer.parseInt(words[2]);
                        faces = new int[faceCount * 3 * 2];
                    }
                }
                line = bufferedReader.readLine();
            }
            // end of header

            line = bufferedReader.readLine();

            int indexVertex = 0;
            int indexTexCoord = 0;
            int indexFaces = 0;

            // start of vertex, normal, texCoords
            // the array to visualize:
            // 0.0 0.0 -1.0 0.187597 -0.577354 -0.794651 0.673885 0.544068
            //  0   1    2     3         4         5        6         7
            for (int i = 0; i < vertexCount; i++) {
                if (line == null) {
                    System.out.println("Unexpected end of mesh vertex data!");
                    break;
                }

                String[] numbers = line.split(" ");
                if (numbers.length == 5) {
                    // put the first the into points
                    for (int j = 0; j < 3; j++, indexVertex++) {
                        // 0, 1, 2 == x, y, z
                        points[indexVertex] = Float.parseFloat(numbers[j]);
                    }

                    // put the last two into tex coords
                    texCoords[indexTexCoord] = Float.parseFloat(numbers[numbers.length - 2]);
                    texCoords[indexTexCoord + 1] = Float.parseFloat(numbers[numbers.length - 1]);
                    indexTexCoord += 2;
                } else {
                    System.out.println("Mesh vertex attribute format incorrect!");
                }

                line = bufferedReader.readLine();
            }

            // start of faces
            // the array to visualize:
            // 3 51 52 53
            for (int i = 0; i < faceCount; i++) {
                if (line == null) {
                    System.out.println("Unexpected end of mesh face data!");
                    break;
                }

                String[] numbers = line.split(" ");

                if (numbers.length == 4 && numbers[0].equals("3")) {
                    for (int j = 1; j < 4; j++) {
                        faces[indexFaces]     = Integer.parseInt(numbers[j]);
                        faces[indexFaces + 1] = Integer.parseInt(numbers[j]);

                        indexFaces += 2;
                    }
                } else {
                    System.out.println("Mesh face format incorrect!");
                }

                line = bufferedReader.readLine();
            }

            // sets all points
            mesh.getPoints().setAll(points);
            mesh.getTexCoords().setAll(texCoords);
            mesh.getFaces().setAll(faces);

            return mesh;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
