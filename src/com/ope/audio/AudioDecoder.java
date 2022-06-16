package com.ope.audio;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBVorbisInfo;

import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryUtil.*;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.DecoderException;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.SampleBuffer;

public class AudioDecoder 
{
	public static class SoundInfo
	{
		int channels,samplerate;
	}
    
    public static ShortBuffer decodeMPEG(InputStream input,SoundInfo info) throws BitstreamException, DecoderException, IOException
    {
    	ArrayList<ShortBuffer> frames = new ArrayList<ShortBuffer>();
    	
    	Bitstream bitstream = new Bitstream(input);
    	Decoder decoder = new Decoder();
    	
    	int divider = 1;
    	
    	while (true)
    	{
    		Header header = bitstream.readFrame();
    		
    		if (header == null)
    			break;
    		
    		SampleBuffer sb = (SampleBuffer)decoder.decodeFrame(header, bitstream);
   		
        	if (divider == 1) 
        	{
				if (decoder.getOutputFrequency() <= 24000)
					divider *= 2;
				if (decoder.getOutputChannels() == 1)
					divider *= 2;
			}
        	
			ShortBuffer sbuf = memAllocShort(decoder.getOutputBlockSize());
    		sbuf.put(sb.getBuffer());
    		sbuf.flip();
    		
    		frames.add(sbuf);
    		
    		bitstream.closeFrame();
    	}
    	
    	info.channels = decoder.getOutputChannels();
    	info.samplerate = decoder.getOutputFrequency();
    	
    	ShortBuffer buf = memAllocShort(frames.size() * decoder.getOutputBlockSize() / divider);
    	for (int i = 0;i < frames.size();i++)
    	{
    		for (int j = 0;j < decoder.getOutputBlockSize() / divider;j++)
    			buf.put(frames.get(i).get(j));
    		memFree(frames.get(i));
    	}
    	frames.clear();
    	
    	input.close();
    	
    	buf.flip();
    	
    	return buf;
    }
    
    public static ShortBuffer decodeVorbis(InputStream input,SoundInfo info) throws IOException
    {
    	byte[] rawfile = input.readAllBytes();
    	input.close();
    	
    	ByteBuffer filebuffer = memAlloc(rawfile.length);
    	filebuffer.put(rawfile);
    	filebuffer.flip();
    	
        IntBuffer error = memAllocInt(1);
        long decoder = stb_vorbis_open_memory(filebuffer, error, null);
        if (decoder == NULL) {
            throw new IOException("Failed to open Ogg Vorbis file. Error: " + error.get(0));
        }
        memFree(error);
        
        STBVorbisInfo vorbisinfo = STBVorbisInfo.malloc();
        
        stb_vorbis_get_info(decoder, vorbisinfo);

        info.channels = vorbisinfo.channels();
        info.samplerate = vorbisinfo.sample_rate();
    	
        ShortBuffer pcm = BufferUtils.createShortBuffer(stb_vorbis_stream_length_in_samples(decoder) * vorbisinfo.channels());

        stb_vorbis_get_samples_short_interleaved(decoder, vorbisinfo.channels(), pcm);
        stb_vorbis_close(decoder);
        
        memFree(filebuffer);

        return pcm;
    	
    }
}
