package org.i3xx.step.due.service.impl;

/*
 * #%L
 * NordApp OfficeBase :: due
 * %%
 * Copyright (C) 2014 - 2015 I.D.S. DialogSysteme GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.i3xx.util.basic.io.FilePath;


public class DeployTool {
	
	static final Pattern pattern = Pattern.compile("\\~generated-\\d{4}-\\d{2}-\\d{2}-\\d{2}-\\d{2}-\\d{2}\\.zip");
	
	//Pattern to check the input (normal [+/]), (urlsafe: [-_]), (filler: [=])
	static final Pattern patProcess = Pattern.compile("[A-Za-z0-9+/=_-]{4,}");
	//Pattern to check the input
	static final Pattern patMandator = Pattern.compile("[A-Za-z][A-Za-z0-9_-]{2,}");
	//Pattern to check the input
	static final Pattern patGroup = Pattern.compile("[a-z][a-z0-9_-]{3,}");
	//Pattern to check the input
	static final Pattern patArtifact = Pattern.compile("[a-z][a-z0-9_-]{3,}");

	public DeployTool() {
	}
	
	/**
	 * Checks the input data
	 * 
	 * @param processId
	 * @param mandatorId
	 * @param groupId
	 * @param artifactId
	 */
	protected void checkArguments(String processId, String mandatorId, String groupId,
			String artifactId) {
		
		if( ! patProcess.matcher(processId).matches())
			throw new IllegalArgumentException("The process doesn't match ("+processId+").");
		if( ! patMandator.matcher(mandatorId).matches())
			throw new IllegalArgumentException("The mandator doesn't match ("+mandatorId+").");
		if( ! patGroup.matcher(groupId).matches())
			throw new IllegalArgumentException("The group doesn't match ("+groupId+").");
		if( ! patArtifact.matcher(artifactId).matches())
			throw new IllegalArgumentException("The artifact doesn't match ("+artifactId+").");
	}
	
	/**
	 * Checks the input data
	 * 
	 * @param processId
	 * @param groupId
	 * @param artifactId
	 */
	protected void checkArguments(String processId, String groupId,
			String artifactId) {
		
		if( ! patProcess.matcher(processId).matches())
			throw new IllegalArgumentException("The process doesn't match ("+processId+").");
		if( ! patGroup.matcher(groupId).matches())
			throw new IllegalArgumentException("The group doesn't match ("+groupId+").");
		if( ! patArtifact.matcher(artifactId).matches())
			throw new IllegalArgumentException("The artifact doesn't match ("+artifactId+").");
	}
	
	/**
	 * @param mandatorPath The path of the mandator
	 * @param processId The process id
	 * @param groupId The group id
	 * @param artifactId The artifact id
	 * @throws IOException
	 */
	protected File createEmptyZipA(String mandatorPath, String processId, String groupId, String artifactId) throws IOException {
		
		checkArguments(processId, groupId, artifactId);
		
		//FilePath filePath = FilePath.append( mandatorPath, "data/page/file/bundle", groupId, artifactId );
		//FilePath binPath = FilePath.append( mandatorPath, "data/step/bin/bundle", groupId, artifactId );
		FilePath tmpPath = FilePath.append( mandatorPath, "temp/deploy", processId );
		FilePath artPath = tmpPath.add( groupId ).add( artifactId );
		
		FilePath fPr = artPath.add("properties");
		FilePath fRs = artPath.add("resources");
		FilePath fJs = artPath.add("js");
		
		//build struct
		buildStruct(fPr, fRs, fJs);
		//create the ZIP file
		File zip = fileZip(tmpPath);
		//clean the directory
		cleanDir(tmpPath.toFile());
		
		return zip;
	}
	
	/**
	 * @param mandatorPath The path of the mandator
	 * @param processId The process id
	 * @param groupId The group id
	 * @param artifactId The artifact id
	 * @throws IOException
	 */
	protected File zipFromDataA(String mandatorPath, String processId,
			String groupId, String artifactId) throws IOException {
		
		checkArguments(processId, groupId, artifactId);
		
		FilePath filePath = FilePath.append( mandatorPath, "data/page/file/bundle", groupId, artifactId );
		FilePath binPath = FilePath.append( mandatorPath, "data/step/bin/bundle", groupId, artifactId );
		FilePath tmpPath = FilePath.append( mandatorPath, "temp/deploy", processId );
		FilePath artPath = tmpPath.add( groupId ).add( artifactId );
		
		FilePath fPr = artPath.add("properties");
		FilePath fRs = artPath.add("resources");
		FilePath fJs = artPath.add("js");
		
		//build struct
		buildStruct(fPr, fRs, fJs);
		//copy
		copyDir(filePath.add("properties").toFile(), fPr.toFile());
		copyDir(filePath.add("resources").toFile(), fRs.toFile());
		copyDir(binPath.add("js").toFile(), fJs.toFile());
		//create the ZIP file
		File zip = fileZip(tmpPath);
		//clean the directory
		cleanDir(tmpPath.toFile());
		
		return zip;
	}
	
	/**
	 * @param mandatorPath
	 * @param processId
	 * @param groupId
	 * @param artifactId
	 * @throws IOException
	 */
	protected void zipToDataA(String mandatorPath, String processId, String groupId,
			String artifactId, String zipFileName) throws IOException {
		
		checkArguments(processId, groupId, artifactId);
		
		FilePath filePath = FilePath.append( mandatorPath, "data/page/file/bundle", groupId, artifactId );
		FilePath binPath = FilePath.append( mandatorPath, "data/step/bin/bundle", groupId, artifactId );
		FilePath tmpPath = FilePath.append( mandatorPath, "temp/deploy", processId );
		FilePath artPath = tmpPath.add( groupId ).add( artifactId );
		
		FilePath fPr = artPath.add("properties");
		FilePath fRs = artPath.add("resources");
		FilePath fJs = artPath.add("js");
		
		//build struct
		buildStructA(fPr, fRs, fJs);
		//process the ZIP file
		File zip = new File(tmpPath.toFile(), zipFileName);
		fileUnzip(zip, tmpPath);
		//copy
		copyDir(fPr.toFile(), filePath.add("properties").toFile());
		copyDir(fRs.toFile(), filePath.add("resources").toFile());
		copyDir(fJs.toFile(), binPath.add("js").toFile());
		//clean the directory
		cleanDir(tmpPath.toFile());
	}
	
	/**
	 * @param mandatorPath
	 * @param processId
	 * @return
	 */
	protected String getLatestZipFile(String mandatorPath, String processId) {
		
		List<String> list = new ArrayList<String>();
		FilePath tmpPath = FilePath.append( mandatorPath, "temp/deploy", processId );
		File[] files = tmpPath.toFile().listFiles();
		for(int i=0;files!=null && i<files.length;i++) {
			String name = files[i].getName();
			//skip generated files
			if( ! pattern.matcher(name).matches()){
				continue;
			}
			list.add(name);
		}//for
		Collections.sort(list);
		
		return list.isEmpty() ? null : list.get(list.size()-1);
	}
	
	/**
	 * CRC checksum for test purposes
	 * 
	 * @param mandatorPath
	 * @param processId
	 * @param groupId
	 * @param artifactId
	 * @return
	 * @throws IOException 
	 */
	protected long processCRC(String mandatorPath, String processId, String groupId,
			String artifactId) throws IOException {
		
		checkArguments(processId, groupId, artifactId);
		
		FilePath filePath = FilePath.append( mandatorPath, "data/page/file/bundle", groupId, artifactId );
		FilePath binPath = FilePath.append( mandatorPath, "data/step/bin/bundle", groupId, artifactId );
		
		FilePath fPr = filePath.add("properties");
		FilePath fRs = filePath.add("resources");
		FilePath fJs = binPath.add("js");
		
		//search the files
		List<File> list = new ArrayList<File>();
		fileWalker(fPr.toFile(), list);
		fileWalker(fRs.toFile(), list);
		fileWalker(fJs.toFile(), list);
		
		Collections.sort(list);
		
		CRC32 crc = new CRC32();
		for(File f : list){
			FileInputStream is = new FileInputStream(f);
			int len = 0;
			byte[] buf = new byte[1024];
			
			try{
				while((len=is.read(buf))>-1){
					crc.update(buf, 0, len);
			}
			}finally{
				is.close();
			}
		}//for
		
		return crc.getValue();
	}
	
	/**
	 * Builds the struct of the working directory
	 * 
	 * @param fPr
	 * @param fRs
	 * @param fJs
	 * @throws IOException 
	 */
	private void buildStruct(FilePath fPr, FilePath fRs, FilePath fJs) throws IOException {
		
		File tmp = fPr.toFile();
		if( ! tmp.exists() ){
			tmp.mkdirs();
		}//fi
		
		if( tmp.listFiles()==null ) {
			File txt = new File(tmp, "readme.txt");
			Writer w = new FileWriter(txt);
			try{
				w.write("Put the properties to this directory.\n");
				w.write("The upload will move the files to the right place.\n");
			}finally{
				w.close();
			}
		}//fi
		
		tmp = fRs.toFile();
		if( ! tmp.exists() ){
			tmp.mkdirs();
		}//fi
		if( tmp.listFiles()==null ) {
			File txt = new File(tmp, "readme.txt");
			Writer w = new FileWriter(txt);
			try{
				w.write("Put the resources (HTML, CSS, JS) to this directory or any subdirectory.\n");
				w.write("The upload will move the files to the right place.\n");
			}finally{
				w.close();
			}
		}//fi
		
		tmp = fJs.toFile();
		if( ! tmp.exists() ){
			tmp.mkdirs();
		}//fi
		if( tmp.listFiles()==null ) {
			File txt = new File(tmp, "readme.txt");
			Writer w = new FileWriter(txt);
			try{
				w.write("Put the server scripts (JS) to this directory or any subdirectory.\n");
				w.write("The upload will move the files to the right place.\n");
			}finally{
				w.close();
			}
		}//fi
	}
	
	/**
	 * Builds the structure of the group/artifact directory
	 * 
	 * @param fPr
	 * @param fRs
	 * @param fJs
	 * @throws IOException 
	 */
	private void buildStructA(FilePath fPr, FilePath fRs, FilePath fJs) throws IOException {
		
		File tmp = fPr.toFile();
		if( ! tmp.exists() ){
			tmp.mkdirs();
		}//fi
		
		tmp = fRs.toFile();
		if( ! tmp.exists() ){
			tmp.mkdirs();
		}//fi
		
		tmp = fJs.toFile();
		if( ! tmp.exists() ){
			tmp.mkdirs();
		}//fi
	}
	
	/**
	 * Builds a ZIP file
	 * 
	 * @param tmpPath
	 * @return
	 * @throws IOException
	 */
	private File fileZip(FilePath tmpPath) throws IOException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy'-'MM'-'dd'-'HH'-'mm'-'ss");
		String timestamp = sdf.format(new Date());
		String filename = "~generated-"+timestamp+".zip";
		
		if( ! pattern.matcher(filename).matches() ){
			throw new IOException("The filename '"+filename+
					"' doesn't match the pattern '"+pattern.toString()+"'.");
		}
		
		//search the files
		List<File> list = new ArrayList<File>();
		fileWalker(tmpPath.toFile(), list);
		
		int n = tmpPath.toFile().getAbsolutePath().length();
		File file = tmpPath.add(filename).toFile();
		ZipOutputStream os = new ZipOutputStream(new FileOutputStream(file));
		os.setLevel(Deflater.DEFAULT_COMPRESSION);
		
		try{
			for(File f : list){
				String name = f.getAbsolutePath();
				if(name.length()<=n){
					//incorrect file
					continue;
				}
				//convert separator char to '/' and skip leading '/'
				n = (name.charAt(n)==File.separatorChar) ? n+1 : n;
				name = name.substring(n).replace(File.separatorChar, '/');
				
				ZipEntry e = new ZipEntry( name );
				os.putNextEntry(e);
				
				InputStream in = new FileInputStream(f);
				try{
					int len = 0;
					int readCount = 0;
					byte[] buf = new byte[1024];
					long time = System.currentTimeMillis();
					
					while((len=in.read(buf))>-1){
						os.write(buf, 0, len);
						readCount += len;
					}//while
					
					e.setSize(readCount);
					e.setTime(time);
				}finally{
					in.close();
				}
				os.closeEntry();
				
			}//for
		}finally{
			os.finish();
			os.close();
		}
		return file;
	}
	
	/**
	 * @param zipFile The ZIP file
	 * @param tmpPath The target path
	 * @throws ZipException
	 * @throws IOException
	 */
	private void fileUnzip(File zipFile, FilePath tmpPath) throws ZipException, IOException {
		
		ZipFile zip = new ZipFile(zipFile);
		try{
			Enumeration<? extends ZipEntry> entries = zip.entries();
			while(entries.hasMoreElements()) {
				
				ZipEntry entry = entries.nextElement();
				File dest = tmpPath.add( entry.getName() ).toFile();
				
				//ensure the directory structure
				File path = dest.getParentFile();
				if( path!=null && (! path.exists()) ) {
					path.mkdirs();
				}
				
				OutputStream os = new FileOutputStream(dest);
				InputStream is = zip.getInputStream(entry);
				
				int len = 0;
				byte[] buf = new byte[1024];
				
				try{
					while((len=is.read(buf))>-1){
						os.write(buf, 0, len);
				}
				}finally{
					try{
						is.close();
					}finally{
						os.close();
					}
				}
				
			}//while
		}finally{
			zip.close();
		}
	}
	
	/**
	 * @param cur The current directory to walk in.
	 * @param list The list containing the results
	 */
	private void fileWalker(File cur, List<File> list) {
		File[] files = cur.listFiles();
		for(int i=0;files!=null && i<files.length;i++) {
			if(files[i].isFile()){
				String name = files[i].getName();
				//skip generated files
				if(pattern.matcher(name).matches()){
					continue;
				}
				list.add(files[i]);
			}
			else if(files[i].isDirectory()){
				fileWalker(files[i], list);
			}
		}//for
	}
	
	/**
	 * Copy the content of the directory fromDir into 
	 * the directory toDir.
	 * 
	 * @param fromDir The directory to copy
	 * @param toDir The directory to copy to
	 * @throws IOException
	 */
	private void copyDir(File fromDir, File toDir) throws IOException {
		String fromName = fromDir.getAbsolutePath();
		String toName = toDir.getAbsolutePath();
		
		//search the files
		List<File> list = new ArrayList<File>();
		fileWalker(fromDir, list);
		
		int n = fromName.length();
		for(File src : list) {
			String fn = src.getAbsolutePath();
			if(fn.length()<=n){
				continue;
			}
			fn = toName+fn.substring(n);
			File dest = new File(fn);
			
			copyFile(src, dest);
		}//for
	}
	
	/**
	 * Cleans the directory
	 * 
	 * @param dir
	 */
	private void cleanDir(File file) {
		
		//recursion
		if(file.isDirectory()){
			for(File f : file.listFiles()) {
				cleanDir(f);
			}//for
		}//fi
		
		//do not clean generated zip files
		if(file.isFile() && pattern.matcher(file.getName()).matches()) {
			return;
		}
		
		//cleanup
		file.delete();
		
		//kill it - if possible
		if(file.exists()) {
			try{
				//set the length to o
				RandomAccessFile raf = new RandomAccessFile(file, "rws");
				try{
					raf.setLength(0);
				}finally{
					raf.close();
				}
			}catch(IOException e){}
			
			file.deleteOnExit();
		}//fi
	}
	
	/**
	 * @param file The directory containing the files
	 * @param skipLatest The number of files to behold
	 */
	protected void cleanResultZip(File file, int skipLatest) {
		
		//behold the latest file
		if(skipLatest < 1)
			skipLatest = 1;
		
		if(file.isDirectory()){
			File[] files = file.listFiles();
			if(files!=null){
				Arrays.sort(files, new Comparator<File>(){

					public int compare(File a, File b) {
						return a.getName().compareTo(b.getName());
					}});
			}//fi
			
			for(int i=0;files!=null && i<(files.length-skipLatest);i++) {
				File f = files[i];
				//clean generated zip files
				if(f.isFile() && pattern.matcher(f.getName()).matches()) {
					boolean fd = f.delete();
					
					//kill it - if possible
					if( !fd && file.exists()) {
						try{
							//set the length to o
							RandomAccessFile raf = new RandomAccessFile(file, "rws");
							try{
								raf.setLength(0);
							}finally{
								raf.close();
							}
						}catch(IOException e){}
						
						file.deleteOnExit();
					}//fi
				}//fi
			}//for
		}//fi
	}
	
	/**
	 * Copy one file
	 * 
	 * @param from The file to copy
	 * @param to The result file
	 * @throws IOException
	 */
	private void copyFile(File from, File to) throws IOException {
		//do not overwrite existing file
		if(to.exists())
			return;
		
		//ensure the directory structure
		File path = to.getParentFile();
		if( path!=null && (! path.exists())) {
			path.mkdirs();
		}
		
		int len = 0;
		byte[] buf = new byte[1024];
		
		InputStream in = new FileInputStream(from);
		OutputStream os = new FileOutputStream(to);
		
		try{
			while((len=in.read(buf))>-1){
				os.write(buf, 0, len);
			}//while
		}finally{
			try{
				in.close();
			}finally{
				os.close();
			}
		}
	}
	
} /*CLASS*/
