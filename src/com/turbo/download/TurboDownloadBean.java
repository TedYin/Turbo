package com.turbo.download;

import java.io.File;

/**
 * 下载信息实体类
 * @author Ted
 */
public class TurboDownloadBean {
	
	private String spec;				//目标资源URL
	private long chunkLength = 0;		//分片大小 TODO:下一次迭代时考虑多线程下载。
	private long startPosition = 0;		//分片开始位置
	private File outFile;				//目标文件
	private long totoalSrcSize = 0L;	//目标资源的总大小
	private String status = "";         //下载状态
	
	public TurboDownloadBean(String spec,long totalSrcSize,File outFile){
		this.spec = spec;
		this.startPosition = outFile.length();
		this.totoalSrcSize = totalSrcSize;
		this.outFile = outFile;
	}
	
	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public long getChunkLength() {
		return chunkLength;
	}

	public void setChunkLength(long chunkLength) {
		this.chunkLength = chunkLength;
	}

	public long getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(long startPosition) {
		this.startPosition = startPosition;
	}

	public File getOutFile() {
		return outFile;
	}

	public void setOutFile(File outFile) {
		this.outFile = outFile;
	}

	public long getTotoalSrcSize() {
		return totoalSrcSize;
	}

	public void setTotoalSrcSize(long totoalSrcSize) {
		this.totoalSrcSize = totoalSrcSize;
	}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
