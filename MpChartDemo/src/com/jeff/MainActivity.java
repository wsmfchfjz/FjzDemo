package com.jeff;

import java.util.ArrayList;
import java.util.List;

import com.example.mpchartdemo.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends Activity implements OnChartGestureListener{

	private NoScrollViewPager viewPager;
	private List<LineChart> chartList;
	private int currentChartListIndex;
	private ArrayList<String> xVals120;
	private int x_length = 120;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}
	
	private void initView(){
		viewPager = (NoScrollViewPager) findViewById(R.id.viewpager);
		chartList = new ArrayList<LineChart>();
		
		xVals120 = new ArrayList<String>();
		for (int i = 0; i <= x_length; i++) {
			xVals120.add(i + "");
		}
		initChartByDate();
		initChartByDate();
		
		viewPager.setAdapter(new MyAdapter());
		viewPager.setCurrentItem(currentChartListIndex);
	}
	
	private void initChartByDate() {
		LineChart mChart = new LineChart(this);
		chartList.add(mChart);

		mChart.setOnChartGestureListener(this);
		mChart.setDrawGridBackground(false);
		// no description text
		mChart.setDescription("");
		mChart.setNoDataText("");
		mChart.setNoDataTextDescription("");
		mChart.setTouchEnabled(true);// 设置是否可以触摸
		mChart.setDragEnabled(true);// 是否可以拖拽
		mChart.setScaleEnabled(true);// 是否可以缩放
		mChart.setPinchZoom(true);// true表示按两个手指比例缩放，false表示按xy轴比例缩放
		mChart.getLegend().setEnabled(false);
		mChart.setScaleYEnabled(true);// y轴可拉伸

		YAxis leftAxis = mChart.getAxisLeft();
		leftAxis.removeAllLimitLines();
		leftAxis.setAxisMaxValue(549f);// y轴最大值
		leftAxis.setAxisMinValue(0.001f);// y轴最小值
		leftAxis.setLabelCount(10);
		leftAxis.setStartAtZero(false);
		leftAxis.enableGridDashedLine(0f, 0f, 0f);
		leftAxis.setAxisLineColor(Color.WHITE);
		leftAxis.setAxisLineWidth(1.6f);
		leftAxis.setGridColor(Color.parseColor("#888888"));
		leftAxis.setGridLineWidth(0.5f);
		leftAxis.setTextColor(Color.BLACK);
		mChart.getAxisRight().setEnabled(false);
		XAxis xAxis = mChart.getXAxis();
		xAxis.setPosition(XAxisPosition.BOTTOM);
		xAxis.setAxisLineColor(Color.WHITE);
		xAxis.setAxisLineWidth(1.6f);
		xAxis.setGridColor(Color.parseColor("#888888"));
		xAxis.setGridLineWidth(0.5f);
		xAxis.setTextColor(Color.BLACK);

		LineData data = getNewLineData(mChart);
		if (data != null) {
			mChart.setData(data);
			mChart.invalidate();
			mChart.setHighlightEnabled(false);// 设置是否可以高亮
		}
	}

	/**
	 * 读取数据库中的数据，得到最新的LineData
	 */
	private LineData getNewLineData(LineChart mChart) {
		ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
		ArrayList<String> xVals = xVals120;// 初始化x，默认120
		List<Point> pointList = new ArrayList<Point>();
		for (int i = 0; i <= x_length; i++) {
			if(i <= x_length /3 || i >= x_length / 2){//中间虚线，其他地方实线
				Point p = new Point();
				p.setX(i);
				p.setProbe1Temp(100+i);
				p.setProbe2Temp(200+i);
				p.setBtiTemp(300+i);
				pointList.add(p);
			}
		}

		// 画一条透明的曲线
		ArrayList<Entry> yValsNull = new ArrayList<Entry>();
		yValsNull.add(new Entry(1, 0));
		yValsNull.add(new Entry(600, 1));
		LineDataSet set = new LineDataSet(yValsNull, "");
		set.setColor(Color.parseColor("#00000000"));
		set.setDrawValues(false);// 不显示具体数值
		set.setDrawCircles(false);// 不显示具体圆圈
		dataSets.add(set);
		
		if (pointList != null && pointList.size() > 0) {
			int x = x_length;// 默认最大值600

			xVals = xVals120;
			XAxis xAxis = mChart.getXAxis();
			xAxis.setLabelsToSkip(x_length / 12 - 1);

			// 3条曲线
			initLineData(pointList, dataSets, x, 1);
			initLineData(pointList, dataSets, x, 2);
			initLineData(pointList, dataSets, x, 3);
			
			LineData data = new LineData(xVals, dataSets);
			return data;
		}
		LineData data = new LineData(xVals, dataSets);
		return data;
	}

	private ArrayList<LineDataSet> initLineData(List<Point> pointList,ArrayList<LineDataSet> dataSets, int x, int tempType) {
		String color = "";
		switch (tempType) {
		case 1:
			color = "#26ff4a";
			break;
		case 2:
			color = "#ffb951";
			break;
		case 3:
			color = "#00b6ed";
			break;
		}
		List<ArrayList<Entry>> yList = new ArrayList<ArrayList<Entry>>();
		yList.add(new ArrayList<Entry>());
		int yListIndex = 0;
		int pointListIndex = 0;
		boolean isFullLine = true;// 是实线
		for (int i = 0; i <= pointList.get(pointList.size() - 1).getX(); i++) {
			if (pointList.get(pointListIndex).getX() == i) {
				if (isFullLine) {
				} else {
					int firstX = yList.get(yListIndex).get(0).getXIndex();
					float firstY = yList.get(yListIndex).get(0).getVal();
					for (int j = firstX; j <= i; j++) {
						yList.get(yListIndex).add(new Entry(firstY, j));// 添加所有点
					}
					dataSets.add(getLine(false, yList.get(yListIndex), Color.parseColor(color)));// 添加虚线
					isFullLine = true;
					yList.add(new ArrayList<Entry>());
					yListIndex++;
				}
				int y = 0;
				switch (tempType) {
				case 1:
					y = pointList.get(pointListIndex).getProbe1Temp();
					Log.i("btichart", "1:" + y);
					break;
				case 2:
					y = pointList.get(pointListIndex).getProbe2Temp();
					Log.i("btichart", "2:" + y);
					break;
				case 3:
					y = pointList.get(pointListIndex).getBtiTemp();
					Log.i("btichart", "t:" + y);
					break;
				}
				yList.get(yListIndex).add(new Entry(y, i));
				pointListIndex++;
			} else {
				if (isFullLine) {
					dataSets.add(getLine(true, yList.get(yListIndex), Color.parseColor(color)));// 添加实线
					isFullLine = false;
					ArrayList<Entry> yValsnext = new ArrayList<Entry>();// 虚线
					yList.add(yValsnext);
					yListIndex++;
					yList.get(yListIndex).add(yList.get(yListIndex - 1).get(yList.get(yListIndex - 1).size() - 1));
				} else {
				}
			}
			if (i == pointList.get(pointList.size() - 1).getX()) {
				dataSets.add(getLine(true, yList.get(yListIndex), Color.parseColor(color)));// 添加实线
			}
		}
		ArrayList<Entry> yValsEnd = new ArrayList<Entry>();// 虚线
		yValsEnd.add(yList.get(yListIndex).get(yList.get(yListIndex).size() - 1));
		int firstX = yValsEnd.get(0).getXIndex();
		float firstY = yValsEnd.get(0).getVal();
		for (int i = firstX + 1; i <= x; i++) {
			yValsEnd.add(new Entry(firstY, i));// 添加所有点
		}
		dataSets.add(getLine(false, yValsEnd, Color.parseColor(color)));// 添加虚线

		return dataSets;
	}

	private LineDataSet getLine(boolean isFullLine, ArrayList<Entry> yVals, int color) {
		LineDataSet set = new LineDataSet(yVals, "");
		if (!isFullLine) {
			set.enableDashedLine(5, 50, 0);
		}
		set.setColor(color);
		set.setLineWidth(2f);
		set.setCircleSize(3f);
		set.setDrawCircleHole(false);
		set.setValueTextSize(9f);
		set.setFillAlpha(65);
		set.setFillColor(Color.BLACK);
		set.setDrawValues(false);// 不显示具体数值
		set.setDrawCircles(false);// 不显示具体圆圈

		return set;
	}
	
	class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return chartList.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			// super.destroyItem(container, position, object);
			((NoScrollViewPager) container).removeView(chartList.get(position));
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return "title";
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			((NoScrollViewPager) container).addView(chartList.get(position));
			return chartList.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void setPrimaryItem(View container, int position, Object object) {
			currentChartListIndex = position;
		}

	}

	@Override
	public void onChartLongPressed(MotionEvent me) {
		Log.i("LongPress", "Chart longpressed.");
	}

	@Override
	public void onChartDoubleTapped(MotionEvent me) {
		Log.i("DoubleTap", "Chart double-tapped.");
	}

	@Override
	public void onChartSingleTapped(MotionEvent me) {
		Log.i("SingleTap", "Chart single-tapped.");
	}

	@Override
	public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
		Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
	}

	@Override
	public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
		Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
		XAxis xAxis = chartList.get(currentChartListIndex).getXAxis();
		if (chartList.get(currentChartListIndex).getViewPortHandler().canZoomOutMoreX()) {
			viewPager.setNoScroll(true);
			xAxis.resetLabelsToSkip();
		} else {
			viewPager.setNoScroll(false);
			xAxis.setLabelsToSkip(x_length / 12 - 1);
		}
	}

	@Override
	public void onChartTranslate(MotionEvent me, float dX, float dY) {
		Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
	}
}
