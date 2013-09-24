package org.kasource.jmx.core.dashboard;

/**
 * Enumerations of JavaScript functions that might be reused for
 * attribute value transformation by Dashbaord widgets.
 * 
 * @author rikardwi
 **/
public enum JavaScriptFunction {
    BYTES_TO_BYTES("function(value){return value + \" bytes\";}"), 
    BYTES_TO_KB("function(value){return (value/1000) + \" KB\";}"), 
    BYTES_TO_MB("function(value) {return Math.round(value / (1000 * 1000) * 100) / 100 + \" MB\";}"),
    MILLISECONDS_TO_MILLISECONDS("function(value){return value + \" ms\";}"),
    MILLISECONDS_TO_MINUTES("function(value){return Math.round(100*value/(1000*60))/100 + \" minutes\";}"),
    MILLISECONDS_TO_SECONDS("function(value){return Math.round(100*value/(1000))/100 + \" seconds\";}"),
    SECONDS_TO_MINUTES("function(value){return Math.round(100*value/60)/100 + \" minutes\";}"),
    MILLISECONDS_TO_DURATION("function(value) {"
                                + "var days = Math.floor(value / (1000*60*60*24));"
                                + "var hours = Math.floor(value / (1000*60*60)) % 24;"
                                + "var minutes = Math.floor(value / (1000*60)) % 60;"
                                + "var seconds =  Math.floor(value / (1000)) % 60;"
                                + "var result = \"\";"
                                + "if (days == 1) {"
                                + "   result = result + days + \" day \";"           
                                + "} else if (days > 1) {"
                                + "   result = result + days + \" days \";"
                                + "}"
                                + "if(hours == 1) {"
                                + "    result = result + hours + \" hour \";"
                                + "} else if (hours > 1) {"
                                + "    result = result + hours + \" hours \";"
                                + "}"
                                + "if (minutes == 1) {"
                                + "    result = result + minutes + \" minute \";"
                                + "} else if (minutes > 1) {"
                                + "    result = result + minutes + \" minutes \";"
                                + "}"
                                + "if (seconds == 1) {"
                                + "    result = result + seconds + \" second\";"
                                + "} else {"
                                + "    result = result + seconds + \" seconds\";"
                                + "}"
                                + "return result;"
                             + "}"),
    PERCENTAGE("function(value) {return Math.round(value*100) / 100 + \" %\";}"),
    UTC_TO_LOCAL_TIME("function(value) {return new Date(parseInt(value) - new Date().getTimezoneOffset()*60*1000).toJSON().replace(\"T\", \" \").replace(\"Z\",\"\");}");
    
    private String script;
    
     JavaScriptFunction(String script){
        this.script = script;
    }
    
    public String getScript() {
        return script;
    }
}
