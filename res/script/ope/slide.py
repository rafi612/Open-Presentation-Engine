#this is slide_simple lib for OPE. Created by rafi612

projectpath = "";
slides = 0;

fullscreen = True
music = "null"
ttskey = "auto"

slidespath = []
slidesbg = []
slidestts = []
slidesentani  = []
slidesexiani  = []

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
    slidestts.append("null")
    slidesentani.append("null")
    slidesexiani.append("null")

def setSlideBg(num,path):
    global slidesbg;
    del slidesbg[num - 1]
    slidesbg.insert(num - 1,path)
    
def setSlideTTS(num,path):
    global slidestts;
    del slidestts[num - 1]
    slidestts.insert(num - 1,path)
    
def setEntranceAnimation(num,path):
    global slidesentani;
    del slidesentani[num - 1]
    slidesentani.insert(num - 1,path)

def setExitAnimation(num,path):
    global slidesexiani;
    del slidesexiani[num - 1]
    slidesexiani.insert(num - 1,path)

def setFullscreen(arg0):
    global fullscreen
    fullscreen = arg0
        
def setGeneralMusic(m):
    global music
    music = m
    
def setTTSKey(k):
    global ttskey
    ttskey = k
  
def End():
    f = open("config.xml","w+")
    f.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n")
    f.close()
    saveFileWriteLine("config.xml","<class> \n")
    for i in range(slides):
        saveFileWriteLine("config.xml","<slide" + str(i + 1) + ">\n")
        saveFileWriteLine("config.xml","    <path>" + slidespath[i] + "</path> \n");
        saveFileWriteLine("config.xml","    <bg>" + slidesbg[i] + "</bg> \n")
        saveFileWriteLine("config.xml","    <tts>" + slidestts[i] + "</tts> \n")
        saveFileWriteLine("config.xml","    <ent_ani>" + slidesentani[i] + "</ent_ani> \n")
        saveFileWriteLine("config.xml","    <exi_ani>" + slidesexiani[i] + "</exi_ani> \n")
        saveFileWriteLine("config.xml","</slide" + str(i + 1) + "> \n")
    saveFileWriteLine("config.xml","<summary> \n");
    saveFileWriteLine("config.xml","    <slides>" + str(slides) + "</slides> \n")
    saveFileWriteLine("config.xml","    <fullscreen>" + str(fullscreen) + "</fullscreen> \n")
    saveFileWriteLine("config.xml","    <general_music>" + music + "</general_music> \n")
    saveFileWriteLine("config.xml","    <ttskey>" + ttskey + "</ttskey> \n")
    saveFileWriteLine("config.xml","</summary> \n")
    saveFileWriteLine("config.xml","</class>")
    
