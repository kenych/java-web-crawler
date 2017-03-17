package crawler;

import java.util.Optional;

import org.jsoup.select.Elements;

public class Page {
    private String link;

    private Elements links = new Elements();
    private Elements externalLinks = new Elements();
    private Elements images = new Elements();
    private Elements scripts = new Elements();
    private Elements imports = new Elements();
    //add here whatever other resources interested

    private Optional<String> error = Optional.empty();

    public Page(String link, Elements links, Elements externalLinks, Elements images, Elements scripts, Elements imports) {
        this.link = link;
        this.links = links;
        this.externalLinks = externalLinks;
        this.images = images;
        this.scripts = scripts;
        this.imports = imports;
    }

    public Page(String link, String error) {
        this.link = link;
        this.error = Optional.of(error);
    }

    public Optional<String> getError() {
        return error;
    }

    public String getLink() {
        return link;
    }

    public Elements getLinks() {
        return links;
    }

    public Elements getImages() {
        return images;
    }

    public Elements getScripts() {
        return scripts;
    }

    public Elements getImports() {
        return imports;
    }

    public Elements getExternalLinks() {
        return externalLinks;
    }

    @Override
    public String toString() {
        return getError().isPresent() ?
                "Page{ link='" + link + ", error=" + error.get() + '}'
                :
                "Page{ link='" + link +
                        ",\n links=" + links +
                        ",\n externalLinks=" + externalLinks +
                        ",\n images=" + images +
                        ",\n scripts=" + scripts +
                        ",\n imports=" + imports + '}';
    }

}
