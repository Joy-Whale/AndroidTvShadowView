package cn.joy.widget.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User: JiYu
 * Date: 2016-08-26
 * Time: 15-50
 */

public class GridTestActivity extends AppCompatActivity {

	static final int[] IMAGES = {R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4};

	GridView mGridView;
	SimpleAdapter mAdapter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grid);
		mGridView = (GridView) findViewById(R.id.grid);
		initData();
	}

	void initData() {
		List<Item> items = new ArrayList<>();
		for (int i = 0; i < 40; i++) {
			Item item = new Item();
			item.image = IMAGES[new Random().nextInt(4)];
			item.text = "item " + i;
			items.add(item);
		}
		mGridView.setAdapter(mAdapter = new SimpleAdapter(items));
	}

	class SimpleAdapter extends BaseAdapter {
		List<Item> itemList;

		SimpleAdapter(List<Item> itemList) {
			this.itemList = itemList;
		}

		@Override
		public int getCount() {
			return itemList.size();
		}

		@Override
		public Object getItem(int position) {
			return itemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Holder mHolder;
			if (convertView == null) {
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.li_grid_item, parent, false);
				mHolder = new Holder();
				mHolder.img = (ImageView) convertView.findViewById(R.id.li_grid_image);
				mHolder.txt = (TextView) convertView.findViewById(R.id.li_grid_text);
				convertView.setTag(mHolder);
			} else {
				mHolder = (Holder) convertView.getTag();
			}
			final Item item = (Item) getItem(position);
			mHolder.txt.setText(item.text);
			mHolder.img.setImageResource(item.image);
			return convertView;
		}

		class Holder {
			TextView txt;
			ImageView img;
		}
	}

	class Item {
		String text;
		int image;
	}
}
