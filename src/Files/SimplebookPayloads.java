package Files;

public class SimplebookPayloads {
	
    public static String registerPayload(String name, String email)
    {
    return "{\r\n"
            + "   \"clientName\": \""+name+"\",\r\n"
            + "   \"clientEmail\": \""+email+"\"\r\n"
            + "}";    
    }
    
    public static String postOrderPayload(int bookId,String customerName)
    {
        return "{\r\n"
                + "          \"bookId\": "+bookId+",\r\n"
                + "          \"customerName\": \""+customerName+"\"\r\n"
                + "        "
                + "}";
    }
      public static String updateOrderPayload(String customerName)
        {
            return "{\r\n"
                    + "          \"customerName\": \""+customerName+"\"\r\n"
                    + "        "
                    + "}";
        }


}
