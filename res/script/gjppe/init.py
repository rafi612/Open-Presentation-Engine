projectpath = "";
slides = 0;

def initConfig():
    f = open("config.xml","w+");
    f.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n");
    f.close();
    saveFileWriteLine("config.xml","<class> \n");

def saveFileWriteLine(name,text):
	f = open(name,"a+");
	f.write(text);
	f.close()

def saveFile(name,text):
	f = open(name,"w+");
	f.write(text);
	f.close();

def setPath(directory):
	projectpath = directory;
    
def createSlide(num,path):
    global slides;
    slides = slides + 1;
    saveFileWriteLine("config.xml","<slide" + str(num) + ">\n");
    saveFileWriteLine("config.xml","    <path>" + projectpath + path + "</path> \n");
    saveFileWriteLine("config.xml","</slide" + str(num) + "> \n");
    
def End():
    saveFileWriteLine("config.xml","<summary> \n");
    saveFileWriteLine("config.xml","    <slides>" + str(slides) + "</slides> \n");
    saveFileWriteLine("config.xml","</summary> \n");
    saveFileWriteLine("config.xml","</class>");
    
