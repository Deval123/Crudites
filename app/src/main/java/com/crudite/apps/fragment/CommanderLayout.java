package com.crudite.apps.fragment;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.badoualy.stepperindicator.StepperIndicator;
import com.crudite.apps.MainActivity;
import com.crudite.apps.R;
import com.crudite.apps.adapter.ModeReglementAdapter;
import com.crudite.apps.adapter.PlaylistAdapter;
import com.crudite.apps.controller.RequestManager;
import com.crudite.apps.entite.Contenu;
import com.crudite.apps.entite.HoraireLivraison;
import com.crudite.apps.entite.ModeReglement;
import com.crudite.apps.entite.Produit;
import com.crudite.apps.entite.Table;
import com.crudite.apps.utilitaires.KKViewPager;
import com.crudite.apps.utilitaires.Standard;
import com.crudite.apps.utilitaires.ViewPagerAdapter;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;
import com.theartofdev.edmodo.cropper.CropImage;

import org.jdeferred.DoneCallback;
import org.jdeferred.android.AndroidExecutionScope;
import org.jdeferred.android.AndroidFailCallback;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import me.tittojose.www.timerangepicker_library.TimeRangePickerDialog;

public class CommanderLayout{

	private AppCompatActivity context;
	SharedPreferences sharedpreferences;
	private static SlidingUpPanelLayout mLayout;
	static TarifaireFragment tarifaireFragment;
	static ReglementFragment reglementFragment;
	static AttenteFragment attenteFragment;

	public CommanderLayout() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CommanderLayout(AppCompatActivity context){
		this.context = context;
		sharedpreferences = context.getSharedPreferences(Standard.MyPREFERENCES, Context.MODE_PRIVATE);
		onCreateView();
	}
	public void setLayoutReceiver(SlidingUpPanelLayout image){
		this.mLayout = image;
	}
	static StepperIndicator indicator;
	static ViewPager pager;
    public void onCreateView() {
		indicator = (StepperIndicator) context.findViewById(R.id.indicator);
		pager = (ViewPager) context.findViewById(R.id.pager);
		context.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
			}
		});
	}
	Contenu contenu;
	public void setContenu(Contenu contenu){
		this.contenu = contenu;
		load();

	}
	static LieuDegustationFragment lieuDegustationFragment;
	static Lieu2Fragment lieu2Fragment;
	public void load(){
		ImageView image = (ImageView) context.findViewById(R.id.imageDet);
		TextView text = (TextView) context.findViewById(R.id.text);
		TextView subtext = (TextView) context.findViewById(R.id.subtext);

		text.setText(contenu.nom_categorie);
		subtext.setText(contenu.subtext);
		ImageLoader.getInstance().displayImage(Standard.adresse_serveur+"images/contenu/"+contenu.image, image);

		ViewPagerAdapter adapter = new ViewPagerAdapter(context.getSupportFragmentManager());

		lieuDegustationFragment = LieuDegustationFragment.newInstance();
		lieu2Fragment = Lieu2Fragment.newInstance();
		tarifaireFragment = TarifaireFragment.newInstance();
		reglementFragment = ReglementFragment.newInstance();
		attenteFragment = AttenteFragment.newInstance();

		adapter.addFragment(lieuDegustationFragment, "Acceuil");
		adapter.addFragment(lieu2Fragment, "Acceuil");
		adapter.addFragment(tarifaireFragment, "Acceuil");
		adapter.addFragment(reglementFragment, "Acceuil");
		adapter.addFragment(attenteFragment, "Acceuil");
		pager.setAdapter(adapter);
		pager.setOffscreenPageLimit(5);

		indicator.setViewPager(pager);
		indicator.setStepCount(5);
		indicator.setCurrentStep(0);
		post_params = new HashMap<String, String>();
	}
	boolean isSend = false;
	public static Map<String, String> post_params = new HashMap<String, String>();
	public static class LieuDegustationFragment extends Fragment {
		View rootView;
		public LieuDegustationFragment() {
			// Required empty public constructor
		}

		public static LieuDegustationFragment newInstance() {
			LieuDegustationFragment fragment = new LieuDegustationFragment();
			return fragment;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState) {
			// Inflate the layout for this fragment
			rootView = inflater.inflate(R.layout.fragment_lieu_livraison, container, false);

			rootView.findViewById(R.id.card_sur_place).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					pager.setCurrentItem(1);
					indicator.setCurrentStep(1);
					lieu2Fragment.load("livraison");
					post_params.put("type", "livraison");
				}
			});

			rootView.findViewById(R.id.card_restaurant).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					pager.setCurrentItem(1);
					indicator.setCurrentStep(1);
					lieu2Fragment.load("reservation");
					post_params.put("type", "reservation");
				}
			});
			return rootView;
		}
	}
	public static class PopupTable extends DialogFragment {
		private TextView titre, prix, description;
		private ImageView image;
		int position;
		View inflatedView;
		private WormDotsIndicator dotsIndicator;

		public PopupTable(){

		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			inflatedView = inflater.inflate(R.layout.popup_table, null,false);

			getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			getDialog().getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			inflatedView.findViewById(R.id.popup_root).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
				}
			});
			dotsIndicator = (WormDotsIndicator) inflatedView.findViewById(R.id.dots_indicator);

			(new Table()).load(null, true)
					.done(new DoneCallback<ArrayList<?>>() {
						@Override
						public void onDone(ArrayList<?> result) {
							init((ArrayList<Table>)result);
						}
					})
					.fail(new AndroidFailCallback<VolleyError>() {
						@Override
						public void onFail(VolleyError result) {
							Toast.makeText(getActivity(), R.string.verifier_connexion, Toast.LENGTH_LONG).show();
						}

						@Override
						public AndroidExecutionScope getExecutionScope() {
							return null;
						}
					});

			return inflatedView;
		}
		public void init(ArrayList<Table> cates){
			TableAdapter adapter = new TableAdapter(cates);
			adapter.remote = true;
			ViewPager viewPager = (ViewPager) inflatedView.findViewById(R.id.view_pager);
			viewPager.setAdapter(adapter);
			dotsIndicator.setViewPager(viewPager);
		}
		@Override
		public void onDismiss(final DialogInterface dialog) {
			super.onDismiss(dialog);
			//adapter.notifyItemChanged(position);
		}
		public class TableAdapter extends PagerAdapter {
			private final ArrayList<Table> items;
			public boolean remote = false;
			public TableAdapter(ArrayList<Table> items){
				this.items = items;
			}
			@NonNull
			@Override public Object instantiateItem(@NonNull ViewGroup container, int position) {
				View item = LayoutInflater.from(container.getContext())
						.inflate(R.layout.layout_table, container, false);
				ImageView image = (ImageView) item.findViewById(R.id.profile);
				Table contenu = items.get(position);
				ImageLoader.getInstance().displayImage(Standard.adresse_serveur+"images/table/"+contenu.image, image);
				TextView text = (TextView) item.findViewById(R.id.nom);
				HtmlTextView subtext = (HtmlTextView) item.findViewById(R.id.text);
				text.setText(contenu.nom_table);
				subtext.setHtml(contenu.description);
				item.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Lieu2Fragment.table.setText(contenu.nom_table);
						Lieu2Fragment.selectTable = contenu.id_table;
						post_params.put("table", contenu.id_table+"");
						dismiss();
					}
				});
				image.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Lieu2Fragment.table.setText(contenu.nom_table);
						Lieu2Fragment.selectTable = contenu.id_table;
						dismiss();
					}
				});

				container.addView(item);
				return item;
			}

			@Override public int getCount() {
				return items.size();
			}

			@Override public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
				return view == object;
			}

			@Override
			public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
				container.removeView((View) object);
			}

			private class Item {
				private final int color;

				private Item(int color) {
					this.color = color;
				}
			}
		}
	}
	public static class Lieu2Fragment extends Fragment {
		public static View rootView;
        public static final String TIMERANGEPICKER_TAG = "timerangepicker";
		public Lieu2Fragment() {
			// Required empty public constructor
		}

		public static Lieu2Fragment newInstance() {
			Lieu2Fragment fragment = new Lieu2Fragment();
			return fragment;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}
		public static TextView lieu;
		TextView horaire;
		TextView jour;
		TextView heure;
		TextView nbre_personne;
		public static TextView table;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState) {
			// Inflate the layout for this fragment
			rootView = inflater.inflate(R.layout.fragment_lieu_livraison2, container, false);
			TextView icon_lieu = (TextView) rootView.findViewById(R.id.icon_lieu);
			TextView icon_horaire = (TextView) rootView.findViewById(R.id.icon_horaire);
			TextView icon_jour = (TextView) rootView.findViewById(R.id.icon_jour);
			TextView icon_heure = (TextView) rootView.findViewById(R.id.icon_heure);
			TextView icon_nbre_personne = (TextView) rootView.findViewById(R.id.icon_nbre_personne);
			TextView icon_table = (TextView) rootView.findViewById(R.id.icon_table);

			lieu = (TextView) rootView.findViewById(R.id.lieu_livraison);
			horaire = (TextView) rootView.findViewById(R.id.horaire);
			jour = (TextView) rootView.findViewById(R.id.jour);
			heure = (TextView) rootView.findViewById(R.id.heure);
			nbre_personne = (TextView) rootView.findViewById(R.id.nbre_personne);
			table = (TextView) rootView.findViewById(R.id.table);

			Typeface font = Standard.getIconFont(getContext());
			icon_lieu.setTypeface(font);
			icon_horaire.setTypeface(font);
			icon_jour.setTypeface(font);
			icon_nbre_personne.setTypeface(font);
			icon_table.setTypeface(font);
			icon_heure.setTypeface(font);
			rootView.findViewById(R.id.picker_lieu).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					pickPlace();
				}
			});
			rootView.findViewById(R.id.picker_horaire).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					pickHoraire();
				}
			});
			rootView.findViewById(R.id.picker_jour).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					pickDate();
				}
			});
            rootView.findViewById(R.id.picker_heure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickerTime();
                }
            });
			rootView.findViewById(R.id.picker_nbre_personne).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					pickNbrePersonne();
				}
			});
			rootView.findViewById(R.id.picker_table).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					PopupTable pop = new PopupTable();
					pop.setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_DeviceDefault_Dialog);
					pop.show(getFragmentManager(), null);
				}
			});
			rootView.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					go();
				}
			});
			rootView.findViewById(R.id.prev).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					pager.setCurrentItem(0);
				}
			});
			(new HoraireLivraison()).load(null, true)
					.done(new DoneCallback<ArrayList<?>>() {
						@Override
						public void onDone(ArrayList<?> result) {
							listHoraires.removeAll(listHoraires);
							listHoraires.addAll((ArrayList<HoraireLivraison>)result);
						}
					})
					.fail(new AndroidFailCallback<VolleyError>() {
						@Override
						public void onFail(VolleyError result) {
							Toast.makeText(getActivity(), R.string.verifier_connexion, Toast.LENGTH_LONG).show();
						}

						@Override
						public AndroidExecutionScope getExecutionScope() {
							return null;
						}
					});

			return rootView;
		}
		public void go(){
			boolean cancel = false;

			if(rootView.findViewById(R.id.picker_lieu).getVisibility()==View.VISIBLE){
				if(lieu.getText().toString().equals("...")){
					//cancel = true;
					lieu.setError("error");
				}
				else{
					lieu.setError(null);
				}
				post_params.put("lieu", lieu.getText().toString()+"");

				if(selectHoraire==0){
					cancel = true;
					horaire.setError("Champ requis");
				}
				else{
					horaire.setError(null);
					post_params.put("horaire", selectHoraire+"");
				}

			}
			else {
				if(selectJour.length()==0){
					cancel = true;
					jour.setError("Champ requis");
				}
				else{
					jour.setError(null);
					post_params.put("jour", selectJour+"");
				}

				if(selectHeure.length()==0){
					cancel = true;
					heure.setError("Champ requis");
				}
				else{
					heure.setError(null);
					post_params.put("heure", selectHeure+"");
				}

				if(nbrePersonne==0){
					cancel = true;
					nbre_personne.setError("Champ requis");
				}
				else{
					nbre_personne.setError(null);
					post_params.put("nbre_personne", nbrePersonne+"");
				}

				if(selectTable==0){
					cancel = true;
					table.setError("Champ requis");
				}
				else{
					table.setError(null);
					post_params.put("table", selectTable+"");
				}
			}

			if(cancel==false){
				pager.setCurrentItem(2);
				tarifaireFragment.load(type);
			}
		}


		public void pickPlace(){
			PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
			try {
				startActivityForResult(builder.build(getActivity()), MainActivity.PLACE_PICKER_REQUEST);
			} catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
				e.printStackTrace();
			}
		}
		public int horaire_pos = -1;
		public int selectHoraire = 0;
		public String horaire_de_livraison = "", selectJour ="", selectHeure="";
		public int nbrePersonne=0;
		public static int selectTable=0;
		public ArrayList<HoraireLivraison> listHoraires = new ArrayList<>();
		public void pickHoraire(){

			final ArrayList<String> items = new ArrayList<>();
			for (int i=0;i<listHoraires.size();i++){
				items.add(listHoraires.get(i).horaire);
			}
			new AlertDialog.Builder(getContext())
					.setTitle(getString(R.string.horaire_de_livraison))
					.setSingleChoiceItems(items.toArray(new String[items.size()]), horaire_pos, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							horaire_de_livraison = items.get(which).toString();
							horaire_pos = which;
							selectHoraire = listHoraires.get(which).id_horaire;
							horaire.setText(horaire_de_livraison);
							dialog.dismiss();
						}
					})
					.show();
		}
		public  void pickDate(){
			android.app.DatePickerDialog dialog = new android.app.DatePickerDialog(getActivity(), datePickerListener, myCalendar.get(Calendar.YEAR),  myCalendar.get(Calendar.MONTH),  myCalendar.get(Calendar.DAY_OF_MONTH));
			dialog.getDatePicker().setMinDate(System.currentTimeMillis() -1000);

			dialog.show();
		}
        private Calendar myCalendar = Calendar.getInstance();
        private android.app.DatePickerDialog.OnDateSetListener datePickerListener = new android.app.DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year,
								  int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        public void updateLabel(){
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            selectJour = dateFormat.format(myCalendar.getTime());
            jour.setText(selectJour);
        }
        public void pickNbrePersonne(){

            final NumberPicker numberPickerNberPersonne = new NumberPicker(getContext());
            numberPickerNberPersonne.setLayoutParams(new NumberPicker.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            numberPickerNberPersonne.setMaxValue(30);
            numberPickerNberPersonne.setMinValue(1);
            numberPickerNberPersonne.setValue(2);
            numberPickerNberPersonne.setWrapSelectorWheel(false);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.nombre_de_personne));
            builder.setView(numberPickerNberPersonne).setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            nbrePersonne = numberPickerNberPersonne.getValue();
                            nbre_personne.setText(""+nbrePersonne);
                        }
                    });

            final AlertDialog dialogNbrePlat = builder.create();
			dialogNbrePlat.show();
        }
        public void pickerTime(){

            TimeRangePickerDialog timePickerDialog = TimeRangePickerDialog.newInstance(new TimeRangePickerDialog.OnTimeRangeSelectedListener() {
				@Override
				public void onTimeRangeSelected(int startHour, int startMin, int endHour, int endMin) {
					selectHeure = (startHour<10 ? "0"+startHour : startHour) + ":" + (startMin<10 ? "0"+startMin : startMin)+" - "+(endHour<10 ? "0"+endHour : endHour) + ":" + (endMin<10 ? "0"+endMin : endMin);
					heure.setText(selectHeure);
				}
			}, true);
            timePickerDialog.show(getActivity().getSupportFragmentManager(), TIMERANGEPICKER_TAG);
        }
		@Override
		public void setUserVisibleHint(boolean isVisibleToUser) {
			super.setUserVisibleHint(isVisibleToUser);
			if(isVisibleToUser){
				try{
					if(post_params.get("type").equals("livraison")){
						rootView.findViewById(R.id.picker_lieu).setVisibility(View.VISIBLE);
						rootView.findViewById(R.id.picker_horaire).setVisibility(View.VISIBLE);
						rootView.findViewById(R.id.picker_jour).setVisibility(View.GONE);
						rootView.findViewById(R.id.picker_heure).setVisibility(View.GONE);
						rootView.findViewById(R.id.picker_nbre_personne).setVisibility(View.GONE);
						rootView.findViewById(R.id.picker_table).setVisibility(View.GONE);
					}
					else {
						rootView.findViewById(R.id.picker_lieu).setVisibility(View.GONE);
						rootView.findViewById(R.id.picker_horaire).setVisibility(View.GONE);
						rootView.findViewById(R.id.picker_jour).setVisibility(View.VISIBLE);
						rootView.findViewById(R.id.picker_heure).setVisibility(View.VISIBLE);
						rootView.findViewById(R.id.picker_nbre_personne).setVisibility(View.VISIBLE);
						rootView.findViewById(R.id.picker_table).setVisibility(View.VISIBLE);
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		String type = "";
		public void load(final String type){
			this.type = type;
			new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
				@Override
				public void run() {
					if(type.equals("livraison")){
						rootView.findViewById(R.id.picker_lieu).setVisibility(View.VISIBLE);
						rootView.findViewById(R.id.picker_horaire).setVisibility(View.VISIBLE);
						rootView.findViewById(R.id.picker_jour).setVisibility(View.GONE);
						rootView.findViewById(R.id.picker_heure).setVisibility(View.GONE);
						rootView.findViewById(R.id.picker_nbre_personne).setVisibility(View.GONE);
						rootView.findViewById(R.id.picker_table).setVisibility(View.GONE);
					}
					else {
						rootView.findViewById(R.id.picker_lieu).setVisibility(View.GONE);
						rootView.findViewById(R.id.picker_horaire).setVisibility(View.GONE);
						rootView.findViewById(R.id.picker_jour).setVisibility(View.VISIBLE);
						rootView.findViewById(R.id.picker_heure).setVisibility(View.VISIBLE);
						rootView.findViewById(R.id.picker_nbre_personne).setVisibility(View.VISIBLE);
						rootView.findViewById(R.id.picker_table).setVisibility(View.VISIBLE);
					}
				}
			}, 200);
		}
	}
	public static class TarifaireFragment extends Fragment {
		public static View rootView;
		private TextView reserve;

		public TarifaireFragment() {
			// Required empty public constructor
		}

		public static TarifaireFragment newInstance() {
			TarifaireFragment fragment = new TarifaireFragment();
			return fragment;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}
		TextView text;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState) {
			// Inflate the layout for this fragment
			rootView = inflater.inflate(R.layout.fragment_commander_tarifaire, container, false);
			text = (TextView) rootView.findViewById(R.id.text);
			rootView.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					pager.setCurrentItem(3);
					reglementFragment.load(type);
				}
			});
			rootView.findViewById(R.id.prev).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					pager.setCurrentItem(1);
				}
			});
			rootView.findViewById(R.id.reserve).setVisibility(View.VISIBLE);
			reserve = (TextView) rootView.findViewById(R.id.reserve);
			reserve.setText(Html.fromHtml("*Prix réservé aux membres <font color='#000'><b>ACCESS PREMIUM</b></font>, <font color='#000'>dont 5 à 10% systématiquement affectés à une œuvre caritative</font>"));
			text.setText(Html.fromHtml("Contenu intégral du Pack, Thé selon convenance et eau minérale offerte dans une ambiance chaleureuse, adoucissante restauratrice et traitement personnalisé pour <font color='#000'><b>3 000 FCFA</b></font>"));
			return rootView;
		}

		@Override
		public void setUserVisibleHint(boolean isVisibleToUser) {
			super.setUserVisibleHint(isVisibleToUser);
			if(isVisibleToUser){
				try{
					if(post_params.get("type").equals("livraison")){
						text.setText(Html.fromHtml("Packaging <font color='#000'><b>biodégradable</b></font>, frais de livraison et accessoires connexes offerts à <font color='#000'><b>2 500 FCFA</b>*</font>"));
						reserve.setText(Html.fromHtml("*Prix réservé aux membres <font color='#000'><b>ACCESS PREMIUM</b></font>, <font color='#000'><b>dont 5 à 10% systématiquement affectés à une œuvre caritative</b></font>"));
					}
					else {
						text.setText(Html.fromHtml("Contenu intégral du Pack, Thé selon convenance et eau minérale offerte dans une ambiance chaleureuse, adoucissante restauratrice et traitement personnalisé pour <font color='#000'><b>3 000 FCFA</b></font>"));
						reserve.setText(Html.fromHtml("*Prix réservé aux membres <font color='#000'><b>ACCESS PREMIUM</b></font>, <font color='#000'>dont 5 à 10% systématiquement affectés à une œuvre caritative</font>"));
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}

		String type = "";
		public void load(final String type){
			this.type = type;
			new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
				@Override
				public void run() {

				}
			}, 1000);
		}
	}
	public static class ReglementFragment extends Fragment {
		public static View rootView;
		private RecyclerView recyclerView;
		ModeReglementAdapter adapter;
		ArrayList<ModeReglement> list = new ArrayList<>();

		public ReglementFragment() {
			// Required empty public constructor
		}

		public static ReglementFragment newInstance() {
			ReglementFragment fragment = new ReglementFragment();
			return fragment;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState) {
			// Inflate the layout for this fragment
			rootView = inflater.inflate(R.layout.fragment_commander_reglement, container, false);

			recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
			adapter = new ModeReglementAdapter(list, getContext());
			recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
			recyclerView.setAdapter(adapter);
			recyclerView.setNestedScrollingEnabled(false);

			(new ModeReglement()).load(null, true)
					.done(new DoneCallback<ArrayList<?>>() {
						@Override
						public void onDone(ArrayList<?> result) {
							list.removeAll(list);
							list.addAll((ArrayList<ModeReglement>)result);
							adapter.notifyDataSetChanged();
						}
					})
					.fail(new AndroidFailCallback<VolleyError>() {
						@Override
						public void onFail(VolleyError result) {
							Toast.makeText(getActivity(), R.string.verifier_connexion, Toast.LENGTH_LONG).show();
						}

						@Override
						public AndroidExecutionScope getExecutionScope() {
							return null;
						}
					});

			rootView.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if(adapter.selection==-1){
						Toast.makeText(getContext(), R.string.choisir_mode_reglement, Toast.LENGTH_LONG).show();
					}
					else {
						post_params.put("reglement", list.get(adapter.selection).idmode+"");
						if (type.equals("livraison")) {
							save();
						} else {
							pager.setCurrentItem(4);
						}
					}
				}
			});
			rootView.findViewById(R.id.prev).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					pager.setCurrentItem(2);
				}
			});
			return rootView;
		}
		public void save(){
			final ProgressDialog dialog = new ProgressDialog(getActivity());
			dialog.setCancelable(false);
			dialog.setMessage(getString(R.string.veuillez_patienter));
			dialog.show();
			RequestManager.post("Apk/commander", post_params)
					.done(new DoneCallback<String>() {
						@Override
						public void onDone(String result) {
							dialog.hide();
							mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
							Toast.makeText(getActivity(), R.string.commande_enregistre, Toast.LENGTH_LONG).show();
						}
					})
					.fail(new AndroidFailCallback<VolleyError>() {
						@Override
						public void onFail(VolleyError result) {
							dialog.hide();
							Toast.makeText(getActivity(), R.string.verifier_connexion, Toast.LENGTH_LONG).show();
						}

						@Override
						public AndroidExecutionScope getExecutionScope() {
							return null;
						}
					});
		}
		String type = "";
		public void load(final String type){
			this.type = type;
			new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
				@Override
				public void run() {
					if(type.equals("livraison")){
						((TextView)rootView.findViewById(R.id.next)).setText(R.string.commander);
						rootView.findViewById(R.id.reverse).setVisibility(View.GONE);
					}
					else {
						((TextView)rootView.findViewById(R.id.next)).setText(R.string.suivant);
					}
				}
			}, 200);
		}
	}
	public static class AttenteFragment extends Fragment {
		public static View rootView;
		private TextView textMessage;
		private TextView messageAutre;

		public AttenteFragment() {
			// Required empty public constructor
		}

		public static AttenteFragment newInstance() {
			AttenteFragment fragment = new AttenteFragment();
			return fragment;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState) {
			// Inflate the layout for this fragment
			rootView = inflater.inflate(R.layout.fragment_commander_attente, container, false);
			TextView icon_message = (TextView) rootView.findViewById(R.id.icon_message);
			TextView icon_bouteille = (TextView) rootView.findViewById(R.id.icon_bouteille);
			TextView icon_the = (TextView) rootView.findViewById(R.id.icon_the);
			TextView icon_instrumental = (TextView) rootView.findViewById(R.id.icon_instrumental);
			TextView icon_wifi = (TextView) rootView.findViewById(R.id.icon_wifi);
			TextView icon_autre = (TextView) rootView.findViewById(R.id.icon_autre);

			textMessage = (TextView) rootView.findViewById(R.id.textMessage);
			messageAutre = (TextView) rootView.findViewById(R.id.messageAutre);
			textMessage.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					showDialogText(1);
				}
			});

			final Switch switch_message = (Switch) rootView.findViewById(R.id.switch_message);
			final Switch switch_mineral = (Switch) rootView.findViewById(R.id.switch_mineral);
			final Switch switch_the = (Switch) rootView.findViewById(R.id.switch_the);
			final Switch switch_instrumental = (Switch) rootView.findViewById(R.id.switch_instrumental);
			final Switch switch_autre = (Switch) rootView.findViewById(R.id.switch_autre);
			final Switch switch_wifi = (Switch) rootView.findViewById(R.id.switch_wifi);

			switch_message.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked)
						showDialogText(1);
					else {
						message_personnalis = "";
						textMessage.setText(R.string.laisser_un_message_particuler);
					}
				}
			});
			switch_autre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked)
						showDialogText(2);
					else {
						message_autre = "";
						messageAutre.setText("");
						messageAutre.setVisibility(View.GONE);
					}
				}
			});

			Typeface font = Standard.getIconFont(getContext());
			icon_message.setTypeface(font);
			icon_bouteille.setTypeface(font);
			icon_the.setTypeface(font);
			icon_instrumental.setTypeface(font);
			icon_wifi.setTypeface(font);
			icon_autre.setTypeface(font);

			rootView.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					post_params.put("message", message_personnalis+"");
					post_params.put("autre", message_autre+"");
					post_params.put("mineral", (switch_mineral.isChecked() ? 1 : 0) +"");
					post_params.put("the", (switch_the.isChecked() ? 1 : 0) +"");
					post_params.put("wifi", (switch_wifi.isChecked() ? 1 : 0) +"");
					post_params.put("instrumental", (switch_instrumental.isChecked() ? 1 : 0) +"");
					save();
				}
			});
			rootView.findViewById(R.id.prev).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					pager.setCurrentItem(2);
				}
			});
			return rootView;
		}
		String message_personnalis = "", message_autre;
		public void showDialogText(final int element){
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(element==1 ? R.string.message_personnalis : R.string.preciser);

// Set up the input
			final EditText input = new EditText(getActivity());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
			input.setInputType(InputType.TYPE_CLASS_TEXT);
			builder.setView(input);

// Set up the buttons
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(element==1) {
						if (input.getText().toString().length() > 0) {
							message_personnalis = input.getText().toString();
							textMessage.setText(message_personnalis);
						} else {
							textMessage.setText(R.string.laisser_un_message_particuler);
						}
					}
					else {
						if (input.getText().toString().length() > 0) {
							message_autre = input.getText().toString();
							messageAutre.setText(message_autre);
							messageAutre.setVisibility(View.VISIBLE);
						} else {
							messageAutre.setVisibility(View.GONE);
						}
					}
				}
			});
			builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});

			builder.show();
		}
		public void save(){
			final ProgressDialog dialog = new ProgressDialog(getActivity());
			dialog.setCancelable(false);
			dialog.setMessage(getString(R.string.veuillez_patienter));
			dialog.show();
			RequestManager.post("Apk/commander", post_params)
					.done(new DoneCallback<String>() {
						@Override
						public void onDone(String result) {
							dialog.hide();
							Toast.makeText(getActivity(), R.string.commande_enregistre, Toast.LENGTH_LONG).show();
							mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
						}
					})
					.fail(new AndroidFailCallback<VolleyError>() {
						@Override
						public void onFail(VolleyError result) {
							dialog.hide();
							Toast.makeText(getActivity(), R.string.verifier_connexion, Toast.LENGTH_LONG).show();
						}

						@Override
						public AndroidExecutionScope getExecutionScope() {
							return null;
						}
					});
		}

	}
}
