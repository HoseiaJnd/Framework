package jnd.mapping;

public class Mapping {
    private String className;
    private String methodName;
    private String httpVerb; // Ajoutez cet attribut

    public Mapping(String className, String methodName, String httpVerb) {
        this.className = className;
        this.methodName = methodName;
        this.httpVerb = httpVerb;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getHttpVerb() {
        return httpVerb;
    }

    @Override
    public String toString() {
        return "Mapping{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                '}';
    }
}
