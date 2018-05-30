package utils;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Paths;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FilmInfoScrap implements Runnable {
  private String filmName;
  private Document doc; // Wikipedia page for the movie to scrap
  private String imdbLink;
  private String imgLink;

  private static final FileSystem fs = FileSystems.getDefault();
  private static final String basePath = Paths.get(".").toAbsolutePath().normalize().toString();
  private static String datafile =  basePath + fs.getSeparator()
          + "data" + fs.getSeparator() + "data.txt";

  public FilmInfoScrap(String filmName) throws IOException {
    this.filmName = filmName;
    this.doc = Jsoup.connect("https://en.wikipedia.org/wiki/" + filmName).get(); //If other language needed, can change the wikipedia page to other urls.

  }



  public String scrapInfo() {
    String line = new String(filmName);

    Element summary = doc.selectFirst("div.mw-content-ltr > div.mw-parser-output > p");
    Element table = doc.selectFirst("table.infobox tbody");
    Elements rows = table.select("tr");

    imgLink = table.selectFirst("a.image").selectFirst("img").absUrl("src");
    System.out.println(imgLink);

    for (Element row : rows) {
      Element tHead = row.selectFirst("th");
      Element tData = row.selectFirst("td");

      if (tData != null && tHead != null) {
        String key = tHead.text();
        String value = "";
        Element plainList = tData.selectFirst("div.plainlist");
        if (plainList != null) {
          Elements values = plainList.select(("ul li"));
          for (Element listEle : values) {
            if (listEle == values.last()) {
              value += listEle.text();
            } else {
              value += listEle.text() + ",";
            }
          }
        } else {
          value += tData.text();
        }
        //System.out.println(key + " : " + value);
        if (key.startsWith("Directed")) {
          line += ("|" + value);
        } else if (key.startsWith("Starring")){
          line += ("|" + value);
        } else if (key.startsWith("Running")) {
          line += ("|" + value.split("\\s")[0]);
        }
      }

    }
    line += (" |" + summary.text());

    return line;
  }

  public String getIMDbLink() {
    Elements links = doc.select("a.external.text");
    // System.out.println(links.size());
    for (Element element : links) {
      if (element.attr("href").startsWith("https://www.imdb.com/")) {
        imdbLink = element.attr("href");
        //System.out.println(imdbLink);

      }
    }
    return imdbLink;
  }

  public String getImgLink() {
    return imgLink;
  }

  public int writeImg() {
    /*String basePath = Paths.get(".").toAbsolutePath().normalize().toString();
    FileSystem fs = FileSystems.getDefault();*/
    try {
      URL img = new URL(getImgLink());
      ReadableByteChannel rbc = Channels.newChannel(img.openStream());
      FileOutputStream fos = new FileOutputStream(basePath + fs.getSeparator() +
              "data" + fs.getSeparator() + "pictures" + fs.getSeparator() + filmName + ".jpg");
      fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
      fos.close();
      return 0;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return -1;
  }

  public int writeInfo() {
    FileWriter fw = null;
    synchronized (datafile) {
      try {
        fw = new FileWriter(datafile, true);
        BufferedWriter bw = new BufferedWriter(fw);
        //bw.write(scrapInfo() + "|" + fs.getSeparator() +
        //      "data" + fs.getSeparator() + "pictures" + fs.getSeparator() + filmName + ".jpg"  + "\n");
        bw.write(scrapInfo() + "\n");
        bw.close();
        return 0;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return -1;

  }
  @Override
  public void run() {
    scrapInfo();
    writeImg();
    writeInfo();

  }

  public static void main(String[] args) {
    try {
      FilmInfoScrap pulpFiction = new FilmInfoScrap("Pulp Fiction");
      FilmInfoScrap sevenSamurai = new FilmInfoScrap("Seven Samurai");
      FilmInfoScrap thirdMan = new FilmInfoScrap("The Third Man");
      //System.out.println(pulpFiction.scrapInfo());
      //pulpFiction.writeInfo();
      //pulpFiction.writeImg();
      Thread a = new Thread(pulpFiction);
      Thread b = new Thread(sevenSamurai);
      Thread c = new Thread(thirdMan);

      a.start();
      b.start();
      c.start();

      a.join();
      b.join();
      c.join();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}

