package com.android.hoinnet.highde.utils;

import java.util.List;

public final class ShellUtils {

    public static class CommandResult {
        public String errorMsg;
        public int result;
        public String successMsg;

        public CommandResult(int i, String str, String str2) {
            this.result = i;
            this.successMsg = str;
            this.errorMsg = str2;
        }
    }

    private ShellUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static CommandResult execCmd(String str, boolean z) {
        return execCmd(new String[]{str}, z, true);
    }

    public static CommandResult execCmd(List<String> list, boolean z) {
        return execCmd(list == null ? null : (String[]) list.toArray(new String[0]), z, true);
    }

    public static CommandResult execCmd(String[] strArr, boolean z) {
        return execCmd(strArr, z, true);
    }

    public static CommandResult execCmd(String str, boolean z, boolean z2) {
        return execCmd(new String[]{str}, z, z2);
    }

    public static CommandResult execCmd(List<String> list, boolean z, boolean z2) {
        return execCmd(list == null ? null : (String[]) list.toArray(new String[0]), z, z2);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v0, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r15v1, resolved type: java.io.Closeable[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v0, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v1, resolved type: java.io.DataOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: java.lang.StringBuilder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v0, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v2, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v1, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v2, resolved type: java.io.DataOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v7, resolved type: java.io.Closeable[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: java.lang.StringBuilder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v2, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v3, resolved type: java.io.DataOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v1, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r15v8, resolved type: java.lang.StringBuilder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v4, resolved type: java.io.DataOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v3, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r15v9, resolved type: java.lang.StringBuilder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v5, resolved type: java.io.DataOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v4, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v6, resolved type: java.io.DataOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r15v10, resolved type: java.lang.StringBuilder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v8, resolved type: java.io.DataOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v9, resolved type: java.io.DataOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v5, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v5, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v6, resolved type: java.lang.StringBuilder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v1, resolved type: java.io.Closeable[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v7, resolved type: java.lang.StringBuilder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v9, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v10, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v2, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v11, resolved type: java.lang.StringBuilder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v11, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v3, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v11, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v12, resolved type: java.io.BufferedReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v5, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v14, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v16, resolved type: java.io.BufferedReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v1, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v8, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v18, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v13, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v14, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v20, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v15, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v16, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v17, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v21, resolved type: java.lang.String} */
    /* JADX WARNING: type inference failed for: r6v0 */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x009e, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x009f, code lost:
        r8 = null;
        r9 = r1;
        r1 = r13;
        r13 = r7;
        r7 = null;
        r15 = r15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00ae, code lost:
        r15 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00af, code lost:
        r1 = r13;
        r13 = r15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00c7, code lost:
        r13 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00c8, code lost:
        r8 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00ca, code lost:
        r13 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00cb, code lost:
        r15 = null;
        r7 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x00f0, code lost:
        r14.destroy();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x0119, code lost:
        r14.destroy();
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00c7 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:13:0x0025] */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x00f0  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x00f9  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x00fb  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x0102  */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x0119  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.android.hoinnet.highde.utils.ShellUtils.CommandResult execCmd(java.lang.String[] r13, boolean r14, boolean r15) {
        /*
            r0 = 0
            r1 = -1
            if (r13 == 0) goto L_0x011d
            int r2 = r13.length
            if (r2 != 0) goto L_0x0009
            goto L_0x011d
        L_0x0009:
            r2 = 2
            r3 = 3
            r4 = 1
            r5 = 0
            java.lang.Runtime r6 = java.lang.Runtime.getRuntime()     // Catch:{ Exception -> 0x00d9, all -> 0x00d4 }
            if (r14 == 0) goto L_0x0016
            java.lang.String r14 = "su"
            goto L_0x0018
        L_0x0016:
            java.lang.String r14 = "sh"
        L_0x0018:
            java.lang.Process r14 = r6.exec(r14)     // Catch:{ Exception -> 0x00d9, all -> 0x00d4 }
            java.io.DataOutputStream r6 = new java.io.DataOutputStream     // Catch:{ Exception -> 0x00d1, all -> 0x00ce }
            java.io.OutputStream r7 = r14.getOutputStream()     // Catch:{ Exception -> 0x00d1, all -> 0x00ce }
            r6.<init>(r7)     // Catch:{ Exception -> 0x00d1, all -> 0x00ce }
            int r7 = r13.length     // Catch:{ Exception -> 0x00ca, all -> 0x00c7 }
            r8 = r5
        L_0x0027:
            if (r8 >= r7) goto L_0x0040
            r9 = r13[r8]     // Catch:{ Exception -> 0x00ca, all -> 0x00c7 }
            if (r9 != 0) goto L_0x002e
            goto L_0x003d
        L_0x002e:
            byte[] r9 = r9.getBytes()     // Catch:{ Exception -> 0x00ca, all -> 0x00c7 }
            r6.write(r9)     // Catch:{ Exception -> 0x00ca, all -> 0x00c7 }
            java.lang.String r9 = "\n"
            r6.writeBytes(r9)     // Catch:{ Exception -> 0x00ca, all -> 0x00c7 }
            r6.flush()     // Catch:{ Exception -> 0x00ca, all -> 0x00c7 }
        L_0x003d:
            int r8 = r8 + 1
            goto L_0x0027
        L_0x0040:
            java.lang.String r13 = "exit\n"
            r6.writeBytes(r13)     // Catch:{ Exception -> 0x00ca, all -> 0x00c7 }
            r6.flush()     // Catch:{ Exception -> 0x00ca, all -> 0x00c7 }
            int r13 = r14.waitFor()     // Catch:{ Exception -> 0x00ca, all -> 0x00c7 }
            if (r15 == 0) goto L_0x00b2
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00ae, all -> 0x00c7 }
            r15.<init>()     // Catch:{ Exception -> 0x00ae, all -> 0x00c7 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00a6, all -> 0x00c7 }
            r1.<init>()     // Catch:{ Exception -> 0x00a6, all -> 0x00c7 }
            java.io.BufferedReader r7 = new java.io.BufferedReader     // Catch:{ Exception -> 0x009e, all -> 0x00c7 }
            java.io.InputStreamReader r8 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x009e, all -> 0x00c7 }
            java.io.InputStream r9 = r14.getInputStream()     // Catch:{ Exception -> 0x009e, all -> 0x00c7 }
            java.lang.String r10 = "UTF-8"
            r8.<init>(r9, r10)     // Catch:{ Exception -> 0x009e, all -> 0x00c7 }
            r7.<init>(r8)     // Catch:{ Exception -> 0x009e, all -> 0x00c7 }
            java.io.BufferedReader r8 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0097, all -> 0x0093 }
            java.io.InputStreamReader r9 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x0097, all -> 0x0093 }
            java.io.InputStream r10 = r14.getErrorStream()     // Catch:{ Exception -> 0x0097, all -> 0x0093 }
            java.lang.String r11 = "UTF-8"
            r9.<init>(r10, r11)     // Catch:{ Exception -> 0x0097, all -> 0x0093 }
            r8.<init>(r9)     // Catch:{ Exception -> 0x0097, all -> 0x0093 }
        L_0x0078:
            java.lang.String r9 = r7.readLine()     // Catch:{ Exception -> 0x008c }
            if (r9 == 0) goto L_0x0082
            r15.append(r9)     // Catch:{ Exception -> 0x008c }
            goto L_0x0078
        L_0x0082:
            java.lang.String r9 = r8.readLine()     // Catch:{ Exception -> 0x008c }
            if (r9 == 0) goto L_0x00b6
            r1.append(r9)     // Catch:{ Exception -> 0x008c }
            goto L_0x0082
        L_0x008c:
            r9 = move-exception
            r12 = r1
            r1 = r13
            r13 = r9
            r9 = r12
            goto L_0x00e0
        L_0x0093:
            r13 = move-exception
            r8 = r0
            goto L_0x010b
        L_0x0097:
            r8 = move-exception
            r9 = r1
            r1 = r13
            r13 = r8
            r8 = r0
            goto L_0x00e0
        L_0x009e:
            r7 = move-exception
            r8 = r0
            r9 = r1
            r1 = r13
            r13 = r7
            r7 = r8
            goto L_0x00e0
        L_0x00a6:
            r1 = move-exception
            r7 = r0
            r8 = r7
            r9 = r8
            r12 = r1
            r1 = r13
            r13 = r12
            goto L_0x00e0
        L_0x00ae:
            r15 = move-exception
            r1 = r13
            r13 = r15
            goto L_0x00cb
        L_0x00b2:
            r15 = r0
            r1 = r15
            r7 = r1
            r8 = r7
        L_0x00b6:
            java.io.Closeable[] r3 = new java.io.Closeable[r3]
            r3[r5] = r6
            r3[r4] = r7
            r3[r2] = r8
            com.android.hoinnet.highde.utils.CloseUtils.closeIO(r3)
            if (r14 == 0) goto L_0x00f5
            r14.destroy()
            goto L_0x00f5
        L_0x00c7:
            r13 = move-exception
            r8 = r0
            goto L_0x010c
        L_0x00ca:
            r13 = move-exception
        L_0x00cb:
            r15 = r0
            r7 = r15
            goto L_0x00de
        L_0x00ce:
            r13 = move-exception
            r6 = r0
            goto L_0x00d7
        L_0x00d1:
            r13 = move-exception
            r15 = r0
            goto L_0x00dc
        L_0x00d4:
            r13 = move-exception
            r14 = r0
            r6 = r14
        L_0x00d7:
            r8 = r6
            goto L_0x010c
        L_0x00d9:
            r13 = move-exception
            r14 = r0
            r15 = r14
        L_0x00dc:
            r6 = r15
            r7 = r6
        L_0x00de:
            r8 = r7
            r9 = r8
        L_0x00e0:
            r13.printStackTrace()     // Catch:{ all -> 0x010a }
            java.io.Closeable[] r13 = new java.io.Closeable[r3]
            r13[r5] = r6
            r13[r4] = r7
            r13[r2] = r8
            com.android.hoinnet.highde.utils.CloseUtils.closeIO(r13)
            if (r14 == 0) goto L_0x00f3
            r14.destroy()
        L_0x00f3:
            r13 = r1
            r1 = r9
        L_0x00f5:
            com.android.hoinnet.highde.utils.ShellUtils$CommandResult r14 = new com.android.hoinnet.highde.utils.ShellUtils$CommandResult
            if (r15 != 0) goto L_0x00fb
            r15 = r0
            goto L_0x00ff
        L_0x00fb:
            java.lang.String r15 = r15.toString()
        L_0x00ff:
            if (r1 != 0) goto L_0x0102
            goto L_0x0106
        L_0x0102:
            java.lang.String r0 = r1.toString()
        L_0x0106:
            r14.<init>(r13, r15, r0)
            return r14
        L_0x010a:
            r13 = move-exception
        L_0x010b:
            r0 = r7
        L_0x010c:
            java.io.Closeable[] r15 = new java.io.Closeable[r3]
            r15[r5] = r6
            r15[r4] = r0
            r15[r2] = r8
            com.android.hoinnet.highde.utils.CloseUtils.closeIO(r15)
            if (r14 == 0) goto L_0x011c
            r14.destroy()
        L_0x011c:
            throw r13
        L_0x011d:
            com.android.hoinnet.highde.utils.ShellUtils$CommandResult r13 = new com.android.hoinnet.highde.utils.ShellUtils$CommandResult
            r13.<init>(r1, r0, r0)
            return r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.hoinnet.highde.utils.ShellUtils.execCmd(java.lang.String[], boolean, boolean):com.android.hoinnet.highde.utils.ShellUtils$CommandResult");
    }
}
