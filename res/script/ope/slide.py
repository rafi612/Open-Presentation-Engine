#this is slide_simple lib for OPE. Created by Rafał Ploch

projectpath = "";
slides = 0;

fullscreen = True
music = "null"

slidespath = []
slidesbg = []

def saveFileWriteLine(name,text):
    f = open(name,"a+");
    f.write(text);
    f.close()

def saveFile(name,text):
    f = open(name,"w+");
    f.write(text);
    f.close();
    
def createSlide(num,path):
    global slides;
    slides = slides + 1;
    slidespath.append(path)
    slidesbg.append("null")

def setSlideBg(num,path):
    global slidesbg;
    del slidesbg[num - 1]
    slidesbg.insert(num - 1,path)

def setFullscreen(arg0):
    global fullscreen
    fullscreen = arg0
        
def setGeneralMusic(m):
    global music
    music = m
  
def End():
    f = open("config.xml","w+")
    f.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n")
    f.close()
    saveFileWriteLine("config.xml","<class> \n")
    for i in range(slides):
        saveFileWriteLine("config.xml","<slide" + str(i + 1) + ">\n")
        saveFileWriteLine("config.xml","    <path>" + slidespath[i] + "</path> \n");
        saveFileWriteLine("config.xml","    <bg>" + slidesbg[i] + "</bg> \n")
        saveFileWriteLine("config.xml","</slide" + str(i + 1) + "> \n")
    saveFileWriteLine("config.xml","<summary> \n");
    saveFileWriteLine("config.xml","    <slides>" + str(slides) + "</slides> \n")
    saveFileWriteLine("config.xml","    <fullscreen>" + str(fullscreen) + "</fullscreen> \n")
    saveFileWriteLine("config.xml","    <general_music>" + music + "</general_music> \n")
    saveFileWriteLine("config.xml","    <liblary>slide_simple</liblary> \n")
    saveFileWriteLine("config.xml","</summary> \n")
    saveFileWriteLine("config.xml","</class>")
    
