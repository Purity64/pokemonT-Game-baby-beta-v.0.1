import java.io.*;
import java.nio.file.*;
import java.util.UUID;

public class HardwareID {

    public static String getHWID() {
        // 1. กำหนด Path ไปที่ res/hwid ในโปรเจค
        String dirPath = "res/hwid";
        String filePath = dirPath + "/token.txt";

        try {
            // 2. ตรวจสอบและสร้างโฟลเดอร์ถ้ายังไม่มี
            File directory = new File(dirPath);
            if (!directory.exists()) {
                directory.mkdirs(); // สร้างโฟลเดอร์ res และ hwid ให้โดยอัตโนมัติ
            }

            File file = new File(filePath);

            // 3. ถ้ามีไฟล์อยู่แล้ว -> อ่านค่าเดิม
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String token = reader.readLine();
                reader.close();
                if (token != null && !token.trim().isEmpty()) {
                    return token.trim();
                }
            }

            // 4. ถ้ายังไม่มีไฟล์ -> สร้าง Token ใหม่ (UUID) และบันทึก
            String newToken = UUID.randomUUID().toString().substring(0, 16).toUpperCase();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(newToken);
            writer.close();

            System.out.println("LOG: สร้าง Token ใหม่เรียบร้อย: " + newToken);
            return newToken;

        } catch (IOException e) {
            e.printStackTrace();
            // Fallback กรณีเขียนไฟล์ไม่ได้จริงๆ ให้ใช้ชื่อเครื่อง
            return "TEMP_" + System.getProperty("user.name").hashCode();
        }
    }
}