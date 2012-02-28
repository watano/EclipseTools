package com.aureole.watano.game.lol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;

public class LOLItemBuilder {
	public static void main(String[] args) {
		String lolHome = "d:\\game\\Riot Games\\League of Legends\\";
		String character = "Tristana";
		int[] newitems = new int[]{1053, 3009, 3072, 3031, 3022, 3046};

		try {
			String inibin = lolHome+"game\\DATA\\Characters\\"+character+"\\"+character+".inibin";
			File bak = new File(inibin+".bak");
			if(!bak.exists()){
				FileUtils.copyFile(new File(inibin), bak);
			}
			byte[] buff = new byte[]{};
			byte[] bytes = IOUtils.toByteArray(new FileInputStream(bak));
			FileOutputStream fos = new FileOutputStream(inibin);
			byte[] text = new byte[]{};
			for(int i=0; i<bytes.length; i++){
				byte b = bytes[i];
				if(b == 0){
					boolean skip = false;

					if(buff.length == 4*6){//it is item list
						for(int item:newitems){
							fos.write(String.valueOf(item).getBytes());
							fos.write(new byte[]{0});
						}
						buff = new byte[]{};
					}

					if(isText(text)){
						if(buff.length < 4*6 && text.length == 4 && isNumber(text)){
							buff = ArrayUtils.addAll(buff, text);
							skip = true;
						}else if(buff.length > 0){
							for(int x=0; x<buff.length; x=x+4){
								fos.write(new byte[]{buff[x],buff[x+1],buff[x+2],buff[x+3],0});
							}
							buff = new byte[]{};
						}

						String str = new String(text);
						System.err.println(i+"---"+Integer.toHexString(i)+"====>"+str);
					}

					if(!skip){
						fos.write(text);
						fos.write(new byte[]{0});
					}
					text = new byte[]{};
				}else{
					text = ArrayUtils.add(text, b);
				}
			}

			IOUtils.closeQuietly(fos);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean isNumber(byte[] text) {
		boolean isNumber = true;
		for(byte ch: text){
			if(ch < 48 || ch > 57){
				isNumber = false;
				break;
			}
		}
		return isNumber;
	}

	private static boolean isText(byte[] text) {
		boolean isString = true;
		if(text.length > 0){
			for(byte ch : text){
				if(ch < 32 || ch > 126){
					isString = false;
					break;
				}
			}
		}else{
			isString = false;
		}
		return isString;
	}
}
