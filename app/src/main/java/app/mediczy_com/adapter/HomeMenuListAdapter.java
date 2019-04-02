package app.mediczy_com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

import app.mediczy_com.R;
import app.mediczy_com.bean.BeanHomeNavigation;
import app.mediczy_com.utility.TextViewFont;


public class HomeMenuListAdapter extends BaseAdapter
{
	Context context;
	String CallingFrom;
	ArrayList<BeanHomeNavigation> mTitle;
	LayoutInflater inflater;
	public int onCurrentParent = 0,onPagerPosition=0;

	public HomeMenuListAdapter(Context context, ArrayList<BeanHomeNavigation> title, String CallingFrom) {
		this.context = context;
		this.mTitle = title;
		this.CallingFrom = CallingFrom;
	}

	@Override
	public int getCount() {
		return mTitle.size();
	}

	@Override
	public Object getItem(int position) {
//		return mTitle[position];
		return mTitle.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}
	public void setSelectionParent(int groupPosition) {
		onCurrentParent= groupPosition;
	}
	public void setPagerPosition(int groupPosition) {
		onPagerPosition= groupPosition;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// Declare Variables
		TextView txtTitle;
		TextViewFont txtIcon;

		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.drawerlistitem, parent, false);
		txtTitle = (TextView) itemView.findViewById(R.id.title);
		txtIcon = (TextViewFont) itemView.findViewById(R.id.title_icon);

		txtTitle.setText(mTitle.get(position).getTitle());
		txtIcon.setText(mTitle.get(position).getIcon());
//		txtIcon.setVisibility(View.GONE);
		return itemView;
	}
}