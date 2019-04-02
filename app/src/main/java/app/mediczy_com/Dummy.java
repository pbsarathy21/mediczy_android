package app.mediczy_com;

/**
 * Created by Prithivi on 11-10-2017.
 */

public class Dummy {

/*    public class CardAdapter extends RecyclerView.Adapter<RecylerView.ViewHolder>{
        private List<CardData> mItems;
        private Context context;
        private SendInfoToCards cardDatas;
        private boolean mainMenu;
        private static final int FIRST = 1;
        private static final int SECOND = 2;

        public CardAdapter(Context c, SendInfoToCards datas, boolean main) {
            super();
            this.context = c;
            this.cardDatas = datas;
            mItems = datas.getList();
            this.mainMenu = main;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
            if(type == SECOND){
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.second_layout, viewGroup, false);
                return new SecondViewViewHolder(v);
            } else {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.first_layout, viewGroup, false);
                return new FirstViewViewHolder(v);
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            if(viewHolder instanceof SecondViewViewHolder){
                SecondViewViewHolder secondViewViewHolder = (SecondViewViewHolder) viewHolder;
                // bind second views
                secondViewViewHolder.imgThumbnail.setImageResource(R.id.img_resource);
            } else {
                FirstViewViewHolder firstViewViewHolder = (FirstViewViewHolder) viewHolder;
                // bind firs view
                firstViewViewHolder.imgThumbnail.setImageResource(R.id.img_resource);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return FIRST;
            } else {
                return SECOND;
            }
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        class FirstViewViewHolder extends RecyclerView.ViewHolder{
            public ImageView imgThumbnail;
            public TextView tvMovie;

            public FirstViewViewHolder(View itemView) {
                super(itemView);
                imgThumbnail = (ImageView)itemView.findViewById(R.id.img_thumbnail);
                tvMovie = (TextView)itemView.findViewById(R.id.tv_movie);
            }
        }

        class SecondViewViewHolder extends RecyclerView.ViewHolder{
            public ImageView imgThumbnail;
            public TextView tvMovie;
            public ImageView imgThumbnail2;
            public TextView tvMovie2;

            public SecondViewViewHolder(View itemView) {
                super(itemView);
                //find viewby id
            }
        }
    }*/
}
