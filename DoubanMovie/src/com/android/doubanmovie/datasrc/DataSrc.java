package com.android.doubanmovie.datasrc;

//�������Դ
public class DataSrc {
	// ��ӳ���������Դ��ַ��ֱ��ȡ�����壬û��ϸ��
	public static final String SHOWPATH = "http://api.douban.com/v2/movie/in_theaters?count=100&udid=10679043401ceb11cc3886e797a42600c3227df4&start=0&client=s%3Amobile%7Cy%3AAndroid+4.1.1%7Co%3Aeng.buildbot.20130830.211311%7Cf%3A42%7Cv%3A2.5.4%7Cm%3ABaidu_Market%7Cd%3Anull%7Ce%3AGenymotion+vbox86t%7Css%3A800x1216&apikey=0b2bdeda43b5688921839c8ecb20399b&city=%E5%8C%97%E4%BA%AC";
	// ������ӳ����ĵ�ַ
	public static final String FURTURESHOWPATH = "http://api.douban.com/v2/movie/coming_soon?count=20&udid=5af148f3d9447a5d4f8be6f0c68ea52c5bf1ce4a&start=0&client=s%3Amobile%7Cy%3AAndroid+4.4%7Co%3A892118%7Cf%3A42%7Cv%3A2.5.4%7Cm%3ABaidu_Market%7Cd%3A000000000000000%7Ce%3Aunknown+generic%7Css%3A480x800&apikey=0b2bdeda43b5688921839c8ecb20399b&city=%E5%8C%97%E4%BA%AC";
	// ���ܽ���ĵ�ַ��BEFOREΪ֮ǰ�ĵ�ַ��AFTERΪ֮��ĵ�ַ
	public static final String INTRODUCEPATHBEFORE = "http://api.douban.com/v2/movie/subject/";
	public static final String INTRODUCEPATHAFTER = "?udid=10679043401ceb11cc3886e797a42600c3227df4&client=s%3Amobile%7Cy%3AAndroid+4.1.1%7Co%3Aeng.buildbot.20130830.211311%7Cf%3A42%7Cv%3A2.5.4%7Cm%3ABaidu_Market%7Cd%3Anull%7Ce%3AGenymotion+vbox86t%7Css%3A800x1216&apikey=0b2bdeda43b5688921839c8ecb20399b&city=%E5%8C%97%E4%BA%AC";

	// ͼƬ����ĵ�ַ��BEFOREΪǰ��ĵ�ַ��AFTERΪ����ĵ�ַ
	public static final String IMAGEPATHBEFORE = "http://api.douban.com/v2/movie/subject/";
	public static final String IMAGEPATHAFTER = "/photos?count=100&udid=5af148f3d9447a5d4f8be6f0c68ea52c5bf1ce4a&client=s%3Amobile%7Cy%3AAndroid+4.4%7Co%3A892118%7Cf%3A42%7Cv%3A2.5.4%7Cm%3ABaidu_Market%7Cd%3A000000000000000%7Ce%3Aunknown+generic%7Css%3A480x800&apikey=0b2bdeda43b5688921839c8ecb20399b&city=%E5%8C%97%E4%BA%AC";

	// ������ַ
	public static final String SCOMMENTBEFORE = "http://api.douban.com/v2/movie/subject/";
	public static final String SCOMMENTAFTER = "/comments?count=20&udid=cf252ec0ef85d4053a509ca02136991c55eb2373&start=20&client=s%3Amobile%7Cy%3AAndroid+4.4%7Co%3A892118%7Cf%3A42%7Cv%3A2.5.4%7Cm%3ABaidu_Market%7Cd%3A000000000000000%7Ce%3Aunknown+generic%7Css%3A480x800&apikey=0b2bdeda43b5688921839c8ecb20399b&city=%E5%8C%97%E4%BA%AC";

	// Ӱ����ַ
	public static final String COMMENTDEEPBEFORE = "http://api.douban.com/v2/movie/subject/";
	public static final String COMMENTDEEPAFTER = "/reviews?count=20&udid=cf252ec0ef85d4053a509ca02136991c55eb2373&client=s%3Amobile%7Cy%3AAndroid+4.4%7Co%3A892118%7Cf%3A42%7Cv%3A2.5.4%7Cm%3ABaidu_Market%7Cd%3A000000000000000%7Ce%3Aunknown+generic%7Css%3A480x800&apikey=0b2bdeda43b5688921839c8ecb20399b&city=%E5%8C%97%E4%BA%AC";
}
