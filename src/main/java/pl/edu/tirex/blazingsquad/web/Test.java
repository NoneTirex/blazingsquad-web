package pl.edu.tirex.blazingsquad.web;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Test
{
    public static void main(String[] args)
    {
        File file = new File("C:\\Users\\Szymon\\Desktop\\prawy górny róg pulpitu\\BuildTools");
        List<File> exclude = new ArrayList<>();
        exclude.add(new File(file, "ShareX"));
        deleteExceptExclude(file, exclude);
    }

    private static void deleteExceptExclude(File src, List<File> excludeFiles)
    {
        File[] listFiles = src.listFiles();
        if (listFiles == null)
        {
            return;
        }
        for (File file : listFiles)
        {
            if (file.isDirectory())
            {
                if (excludeFiles != null && excludeFiles.contains(file))
                {
                    continue;
                }
                deleteExceptExclude(file, excludeFiles);
                String[] fileList = file.list();
                if (fileList == null || fileList.length == 0)
                {
                    file.delete();
                }
                continue;
            }
            if (excludeFiles == null || !excludeFiles.contains(file))
            {
                file.delete();
            }
        }
        String[] fileList = src.list();
        if (fileList == null || fileList.length == 0)
        {
            src.delete();
        }
    }
}

