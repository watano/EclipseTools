package com.aureole.watano.game.wow;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class SpellInfoParse {
	public static String printSpellNames(Map<Long,String> spellNames, long[] ids){
		String names = "";
		for(long id:ids){
			names += "\""+spellNames.get(id)+"\",";
		}
		return names.substring(0,names.length()-1);
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		try {
			Map<String,Object[]> spellInfo = new Hashtable<String, Object[]>();
			Map<Long,String> spellNames = new Hashtable<Long,String>();
			List<String> lines = FileUtils.readLines(new File("Spell.txt"), "utf-8");
			Pattern p = Pattern.compile("_\\[(\\d+)\\]=\\{name_enus:'(.*)',icon:'(.*)'\\};");
			if(lines != null && lines.size()>0){
				for(String line:lines){
					Matcher m = p.matcher(line);
					if(m.find() && m.groupCount() == 3){
						String name = m.group(2);
						name = name.replace(" of ", "Of");
						name = name.replace("\\'s", "S");
						name = name.replace("(", "_");
						name = name.replace(")", "");
						name = name.replace(" ", "");
						Object[] info = spellInfo.get(name);
						List<Long> ids = null;
						if(info != null){
							ids = (List<Long>)info[0];
						}else{
							info = new Object[3];
							ids = new ArrayList<Long>();
						}
						long id = Long.parseLong(m.group(1));
						if(!ids.contains(id)){
							ids.add(id);
						}
						info[0] = ids;
						info[1] = m.group(2);
						info[2] = m.group(3);
						spellNames.put(id, name);
						spellInfo.put(name, info);
					}
				}
			}
			for(String key:spellInfo.keySet()){
				Object[] info = spellInfo.get(key);
				List<Long> ids = (List<Long>)info[0];
				Collections.sort(ids);
				Collections.reverse(ids);
				int buff = 0;//0--none 1-buff 2-debuff
				if(",MarkOftheWild,GiftOftheWild,TigerSFury,Innervate,Thorns,DireBearForm,WildGrowth,TreeOfLife,Wrath,Enrage,FrenziedRegeneration,FeralCharge-Cat,Starfire,Lifebloom,Regrowth,Prowl,Barkskin,Rejuvenation,NatureSGrasp,TravelForm,".contains(","+key+",")){
					buff = 1;
				}else if(",Mangle_Bear,DemoralizingRoar,Mangle_Cat,Rake,Lacerate,InsectSwarm,FaerieFire_Feral,Moonfire,FaerieFire,".contains(","+key+",")){
					buff = 2;
				}
				String out = "['"+key+"']={['ids']={";
				for(Long id:ids){
					out += id+",";
				}
				out= out.substring(0, out.length()-1);
				out +="}," +
				"['name']='"+info[1]+"'," +
				//				"['icon']='"+info[2]+"'," +
				"['buff']="+buff+
				"},";
				System.err.println(out);
			}
			//buffs
			System.err.println(printSpellNames(spellNames,new long[]{48469,48470,53307,22812,24858,33891,9634,783,29166,48443,48441,48451,53251,48461,48465,53312,22842,5229,50213,49376,5215}));
			//debuffs
			System.err.println(printSpellNames(spellNames,new long[]{770,48463,48468,16857,48567,48564,61676,48560,48564,33983,27003,48574,48566}));
			//actions
			System.err.println(printSpellNames(spellNames,new long[]{770,2782,2893,22812,24858,26985,26986,26988,48469,53307,27009,29166,27013,48378,48443,26982,33763}));
			System.err.println(printSpellNames(spellNames,new long[]{2782,2893,22812,53248,26985,26986,26988,48469,53307,27009,29166,27013,48378,48443,26982,33763}));
			System.err.println(printSpellNames(spellNames,new long[]{16857,48378,48570,48574,50213,48577,48566,48469,53307,49376}));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
