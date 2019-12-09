package com.orhanobut.logger;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DiskLogStrategy implements LogStrategy {
    private final Handler handler;

    static class WriteHandler extends Handler {
        private final String folder;
        private final int maxFileSize;

        WriteHandler(Looper looper, String str, int i) {
            super(looper);
            this.folder = str;
            this.maxFileSize = i;
        }

        public void handleMessage(Message message) {
            FileWriter fileWriter;
            String str = (String) message.obj;
            try {
                fileWriter = new FileWriter(getLogFile(this.folder, "logs"), true);
                try {
                    writeLog(fileWriter, str);
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException unused) {
                }
            } catch (IOException unused2) {
                fileWriter = null;
                if (fileWriter != null) {
                    try {
                        fileWriter.flush();
                        fileWriter.close();
                    } catch (IOException unused3) {
                    }
                }
            }
        }

        private void writeLog(FileWriter fileWriter, String str) throws IOException {
            fileWriter.append(str);
        }

        private File getLogFile(String str, String str2) {
            File file = new File(str);
            if (!file.exists()) {
                file.mkdirs();
            }
            File file2 = null;
            File file3 = new File(file, String.format("%s_%s.csv", new Object[]{str2, 0}));
            int i = 0;
            while (file3.exists()) {
                i++;
                File file4 = new File(file, String.format("%s_%s.csv", new Object[]{str2, Integer.valueOf(i)}));
                file2 = file3;
                file3 = file4;
            }
            return (file2 == null || file2.length() >= ((long) this.maxFileSize)) ? file3 : file2;
        }
    }

    public DiskLogStrategy(Handler handler2) {
        this.handler = handler2;
    }

    public void log(int i, String str, String str2) {
        this.handler.sendMessage(this.handler.obtainMessage(i, str2));
    }
}
