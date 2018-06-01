package utils;

import dao.DbConnection;

import java.io.*;
import java.sql.ResultSet;
import java.util.Arrays;

public class FilmInfoParse {
    private static final String DEFAULT_DATA_PATH = "data";
    private static String sContent;
    private static String sDataPath;
    public static void readInfo(String path) {
        byte[] buffer;
        InputStreamReader isr;
        StringBuilder sb = new StringBuilder();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path))) {
            buffer = new byte[1000];
            int bytes_read;
            while ((bytes_read = bis.read(buffer, 0, buffer.length)) != -1) sb.append(new String(Arrays.copyOfRange(buffer, 0, bytes_read)));

        } catch(Exception e) {
            System.err.println(e);
        }
        sContent = sb.toString();
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            sDataPath = args[0];
        } else {
            sDataPath = DEFAULT_DATA_PATH;
        }
        readInfo(sDataPath + "/data.txt");
        String sql;
        String[] all_infos = sContent.split("\n");
        File films_dir = new File(sDataPath + "/films");
        File[] film_types = films_dir.listFiles();
        File intro_dir = new File(sDataPath + "/introductions");
        FileOutputStream fos;
        for (String one_line : all_infos) {
            String[] field = one_line.split("\\|");

            String[] directors = field[1].split(",");
            String[] actors = field[2].split(",");

            try {
                String intro_url = intro_dir.toURI().toURL().toExternalForm();
                File intro_file = new File(intro_dir, field[0] + ".txt");
                if (!intro_file.exists()) {
                    intro_file.createNewFile();
                }
                fos = new FileOutputStream(intro_file);
                fos.write(field[4].getBytes());
                fos.flush();
                sql = "insert into film (name, duration, intro_url) values (" +
                        "\"" + field[0] + "\", \"" + field[3] + "\", \"" + intro_url + "\");";
                try {
                    DbConnection.excute(sql);
                } catch (Exception e) {
                    System.err.println(e);
                }

                sql = "select id from film where name = \"" + field[0] + "\"";
                ResultSet id_res = DbConnection.query(sql);

                int id = id_res.getInt(1);
                int cnt = 0;
                for (String dirtr : directors) {
                    sql = "insert into film_director (id, director) values (" + id + ",\"" + dirtr + "\");";
                    try {
                        DbConnection.excute(sql);
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                    if (++cnt > 3) break;
                }

                cnt = 0;
                for (String actr : actors) {
                    sql = "insert into film_actor (id, actor) values (" + id + ",\"" + actr + "\");";
                    try {
                        DbConnection.excute(sql);
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                    if (++cnt > 3) break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (File type_ : film_types) {
            try {
                File[] films = type_.listFiles();
                for (File film_ : films) {
                    sql = "insert into film (type) values (\"" + type_.getName().split("[.]")[0] + "\") where name = " + film_.getName() + ";";
                    try {
                        DbConnection.excute(sql);
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
