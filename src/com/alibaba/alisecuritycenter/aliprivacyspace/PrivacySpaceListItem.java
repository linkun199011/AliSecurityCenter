package com.alibaba.alisecuritycenter.aliprivacyspace;

import android.graphics.drawable.Drawable;

/**
 * PrivacySpace 的每个Item 的一个bean
 * @author linkun.lk
 *
 */
public class PrivacySpaceListItem {
	private Drawable drawable;
	private String title;
	private String subtitle;
	
	
	public PrivacySpaceListItem() {
	}
	public PrivacySpaceListItem(Drawable drawable, String title, String subtitle) {
		this.drawable = drawable;
		this.title = title;
		this.subtitle = subtitle;
	}
	public Drawable getDrawable() {
		return drawable;
	}
	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
}
