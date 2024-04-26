import java.io.File;
import javax.swing.filechooser.*;

public class imageFilter extends FileFilter{
    public boolean accept (File f) {
        if (f.isDirectory()) {
            return true;
        }
        
        String extension = Utils.getExtension(f);
        if (extension != null) {
            if (extension.equals(Utils.tiff) ||
                extension.equals(Utils.tif) ||
                extension.equals(Utils.gif) ||
                extension.equals(Utils.jpeg) ||
                extension.equals(Utils.jpg) ||
                extension.equals(Utils.png)) {
                    return true;
            } else {
                return false;
            }
    }
        return false;
}
    

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return "Just Image (*.jpg,...)"; 
    }
}
