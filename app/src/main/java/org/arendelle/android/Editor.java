package org.arendelle.android;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Layout;
import android.text.Selection;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Editor extends ActionBarActivity implements OnItemClickListener, OnClickListener, AdapterView.OnItemLongClickListener {

    // left drawer
    private DrawerLayout drawerLayout;
    private ListView leftDrawerListView;
    private ActionBarDrawerToggle actionBarLeftDrawerToggle;
    private ImageButton leftDrawerButtonAdd;
    private ImageButton leftDrawerButtonSettings;
    private View leftDrawerLastSelection;
    private int leftDrawerInitialSelection;
	
	// gui objects
	private EditText textCode;
	private Button keyLoopOpen,
		keyGrammarDivider, 
		keyLoopClose, 
		keySpaceOpen, 
		keySpaceClose, 
		keyConditionOpen, 
		keyConditionClose, 
		keySpaceSign, 
		keySourceSign, 
		keyTab,
		keyDivideSign, 
		keyEqualSign, 
		keyMinusSign, 
		keyPlusSign, 
		keyMultiplySign, 
		keyFunctionSign,
        keyCountSign,
		keyStoredSpaceSign, 
		keyKeySign, 
		keyPowSign,
		keyModuloSign, 
		keyFunctionHeaderOpen, 
		keyFunctionHeaderClose,
        keyTitleSign,
		keyStringSign,
        keyStringInterpolationSign;
	
	/** project folder */
	private File projectFolder;
	
	/** config file */
	private File configFile;
	
	/** main function */
	private File mainFunction;
	
	/** current function */
	private File currentFunction;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);

        // set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.editor_toolbar);
        setSupportActionBar(toolbar);
		
		// get gui objects
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		leftDrawerListView = (ListView) findViewById(R.id.left_drawer);
		textCode = (EditText) findViewById(R.id.text_code);

		keyLoopOpen = (Button) findViewById(R.id.key_loop_open);
		keyGrammarDivider = (Button) findViewById(R.id.key_grammar_divider);
		keyLoopClose = (Button) findViewById(R.id.key_loop_close);
		keySpaceOpen = (Button) findViewById(R.id.key_space_open);
		keySpaceClose = (Button) findViewById(R.id.key_space_close);
		keyConditionOpen = (Button) findViewById(R.id.key_condition_open);
		keyConditionClose = (Button) findViewById(R.id.key_condition_close);
		keySpaceSign = (Button) findViewById(R.id.key_space_sign);
		keySourceSign = (Button) findViewById(R.id.key_source_sign);
		keyTab = (Button) findViewById(R.id.key_tab);
		keyDivideSign = (Button) findViewById(R.id.key_divide_sign);
		keyEqualSign = (Button) findViewById(R.id.key_equal_sign);
		keyMinusSign = (Button) findViewById(R.id.key_minus_sign);
		keyPlusSign = (Button) findViewById(R.id.key_plus_sign);
		keyMultiplySign = (Button) findViewById(R.id.key_multiply_sign);
		keyFunctionSign = (Button) findViewById(R.id.key_function_sign);
        keyCountSign = (Button) findViewById(R.id.key_count_sign);
		keyStoredSpaceSign = (Button) findViewById(R.id.key_stored_space_sign);
		keyKeySign = (Button) findViewById(R.id.key_key_sign);
		keyPowSign = (Button) findViewById(R.id.key_pow_sign);
		keyModuloSign = (Button) findViewById(R.id.key_modulo_sign);
		keyFunctionHeaderOpen = (Button) findViewById(R.id.key_function_header_open);
		keyFunctionHeaderClose = (Button) findViewById(R.id.key_function_header_close);
		keyTitleSign = (Button) findViewById(R.id.key_title_sign);
		keyStringSign = (Button) findViewById(R.id.key_string_sign);
        keyStringInterpolationSign = (Button) findViewById(R.id.key_string_interpolation_sign);
		
		// activate left drawer toggle in action bar
		actionBarLeftDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                // highlight current function if needed
                if (leftDrawerLastSelection == null) {
                    leftDrawerLastSelection = leftDrawerListView.getChildAt(leftDrawerInitialSelection);
                    ((TextView) leftDrawerLastSelection).setTypeface(null, Typeface.BOLD);
                    ((TextView) leftDrawerLastSelection).setTextColor(getResources().getColor(android.R.color.white));
                }

                // close keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textCode.getWindowToken(), 0);

            }
        };
		drawerLayout.setDrawerListener(actionBarLeftDrawerToggle);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		// setup code area
		textCode.setTypeface(Typeface.createFromAsset(getAssets(), "Inconsolata-Bold.ttf"));
		textCode.setTextColor(getResources().getColor(R.color.colorText));
		final CodeHighlighter codeHighlighter = new CodeHighlighter();
		textCode.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void afterTextChanged(Editable editable) {

				// cleanup: remove spans
				ForegroundColorSpan spans[] = editable.getSpans(0, textCode.getText().length(), ForegroundColorSpan.class);
				for (ForegroundColorSpan span : spans) editable.removeSpan(span);

				codeHighlighter.highlight(Editor.this, textCode);
			}

		});

		// setup drawer list
		leftDrawerListView.setOnItemClickListener(this);
        leftDrawerListView.setOnItemLongClickListener(this);
		
		// setup keys
		keyLoopOpen.setOnClickListener(this);
		keyGrammarDivider.setOnClickListener(this);
		keyLoopClose.setOnClickListener(this);
		keySpaceOpen.setOnClickListener(this);
		keySpaceClose.setOnClickListener(this);
		keyConditionOpen.setOnClickListener(this);
		keyConditionClose.setOnClickListener(this);
		keySpaceSign.setOnClickListener(this);
		keySourceSign.setOnClickListener(this);
		keyTab.setOnClickListener(this);
		keyDivideSign.setOnClickListener(this);
		keyEqualSign.setOnClickListener(this);
		keyMinusSign.setOnClickListener(this);
		keyPlusSign.setOnClickListener(this);
		keyMultiplySign.setOnClickListener(this);
		keyFunctionSign.setOnClickListener(this);
        keyCountSign.setOnClickListener(this);
		keyStoredSpaceSign.setOnClickListener(this);
		keyKeySign.setOnClickListener(this);
		keyPowSign.setOnClickListener(this);
		keyModuloSign.setOnClickListener(this);
		keyFunctionHeaderOpen.setOnClickListener(this);
		keyFunctionHeaderClose.setOnClickListener(this);
		keyTitleSign.setOnClickListener(this);
		keyStringSign.setOnClickListener(this);
        keyStringInterpolationSign.setOnClickListener(this);
		
		// get project folder
		projectFolder = new File(getIntent().getExtras().getString("projectFolder"));
		
		// get config file
		configFile = new File(projectFolder, "project.config");

        // read config file
		try {
            HashMap<String, String> properties = Files.parseConfigFile(configFile);

            // get main function
			mainFunction = new File(projectFolder, properties.get("mainFunction"));

            // get current function
            currentFunction = new File(projectFolder, properties.get("currentFunction"));

            // update or import project if needed
            try {
                if (!properties.containsKey("ide")) {
                    updateProject(0);
                } else if (!properties.get("ide").split(";")[0].equals("android")) {
                    // TODO: import
                } else {
                    int version = Integer.valueOf(properties.get("ide").split(";")[1]);
                    if (version < getPackageManager().getPackageInfo(getPackageName(), 0).versionCode) {
                        updateProject(version);
                    } else if (version > getPackageManager().getPackageInfo(getPackageName(), 0).versionCode) {
                        Toast.makeText(this, R.string.toast_downgrade_warning, Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception e) { }

        } catch (Exception e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
			finish();
		}
        setTitle(currentFunction.getName().split(".arendelle")[0]);
		
		// read code
		try {
			textCode.setText(Files.read(currentFunction));
		} catch (Exception e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
			finish();
		}

        // setup header in left drawer
        View header = getLayoutInflater().inflate(R.layout.drawer_listview_header, null);
        ((ImageView) header.findViewById(R.id.drawer_listview_header_preview)).setImageBitmap(BitmapFactory.decodeFile(new File(projectFolder, ".preview.png").getAbsolutePath()));
        ((TextView) header.findViewById(R.id.drawer_listview_header_text)).setText(projectFolder.getName());
        leftDrawerListView.addHeaderView(header, null, false);

        // get buttons in left drawer
        leftDrawerButtonAdd = (ImageButton) header.findViewById(R.id.drawer_listview_header_button_add);
        leftDrawerButtonSettings = (ImageButton) header.findViewById(R.id.drawer_listview_header_button_settings);

        // setup buttons in left drawer
        leftDrawerButtonAdd.setOnClickListener(this);
        leftDrawerButtonSettings.setOnClickListener(this);
		
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		actionBarLeftDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

        // open or close left drawer
		if (actionBarLeftDrawerToggle.onOptionsItemSelected(item)) return true;

		switch (item.getItemId()) {

			// evaluate
			case R.id.action_run:

				try {

					// close keyboard
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(textCode.getWindowToken(), 0);

					// save code
					Files.write(currentFunction, textCode.getText().toString());

					// execute code
					Intent intent = new Intent(this, Screen.class);
					intent.putExtra("code", Files.read(mainFunction));
					intent.putExtra("projectFolder", projectFolder.getAbsolutePath());
					startActivity(intent);

					drawerLayout.closeDrawer(leftDrawerListView);
				} catch (Exception e) {
					Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
				}

				return true;
			
		default:
			return super.onOptionsItemSelected(item);
			
		}
		
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		actionBarLeftDrawerToggle.syncState();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // save code
        try {
            Files.write(currentFunction, textCode.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();;
        }

        // change colors in list
        if (leftDrawerLastSelection != null) {
            ((TextView) leftDrawerLastSelection).setTypeface(null, Typeface.NORMAL);
            ((TextView) leftDrawerLastSelection).setTextColor(getResources().getColor(android.R.color.darker_gray));
        }
        leftDrawerLastSelection = view;
        ((TextView) view).setTypeface(null, Typeface.BOLD);
        ((TextView) view).setTextColor(getResources().getColor(android.R.color.white));

		// load selected file (/function)
        String path = ((TextView) view).getText().toString().replace('.', '/') + ".arendelle";
		currentFunction = new File(projectFolder, path);
		setTitle(currentFunction.getName().split(".arendelle")[0]);
		
		// read code
		try {
			textCode.setText(Files.read(currentFunction));
		} catch (Exception e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
			finish();
		}
		
		// close drawer
		drawerLayout.closeDrawer(leftDrawerListView);

	}

    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, final long id) {

        // create file options dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.dialog_file_options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch(which) {

                    // rename function
                    case 0:
                        onItemClick(parent, view, position, id);
                        showRenameFunctionDialog();
                        break;

                    // delete function
                    case 1:
                        onItemClick(parent, view, position, id);
                        deleteFunction();
                        break;

                }

            }

        });
        builder.show();

        return false;
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_editor, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onClick(View v) {

        // add function
        if(v == leftDrawerButtonAdd) {
            showNewFunctionDialog();
        }

        // open (project) settings
        else if(v == leftDrawerButtonSettings) {
            Intent intent = new Intent(this, Settings.class);
            intent.putExtra("projectFolder", projectFolder.getAbsolutePath());
            startActivity(intent);
        }

        // add pressed key
        else {

            // get text of pressed key
            String keyText;
            if (v == keyTab) {
                keyText = "   ";
            } else {
                keyText = ((Button) v).getText().toString();
            }

            // insert text into code
            int start = Math.max(textCode.getSelectionStart(), 0);
            int end = Math.max(textCode.getSelectionEnd(), 0);
            textCode.getText().replace(Math.min(start, end), Math.max(start, end), keyText, 0, keyText.length());

        }
		
	}

	@Override
	protected void onPause() {
        super.onPause();

		// save code
		try {
			Files.write(currentFunction, textCode.getText().toString());
		} catch (Exception e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();;
		}
		
		// save config file
		try {
			HashMap<String, String> properties = Files.parseConfigFile(configFile);
			properties.put("currentFunction", Files.getRelativePath(projectFolder, currentFunction.getAbsoluteFile()));
            try {
                if (!properties.containsKey("ide") ||
                        Integer.valueOf(properties.get("ide").split(";")[1]) < getPackageManager().getPackageInfo(getPackageName(), 0).versionCode ||
                        !properties.get("ide").split(";")[0].equals("android")) {

                    properties.put("ide", "android;" + String.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionCode));
                }
            } catch (Exception e) {
                properties.put("ide", "android;0");
            }
			Files.createConfigFile(configFile, properties);
		} catch (Exception e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

        // update main function (for example after changed in project settings)
        try {
            mainFunction = new File(projectFolder, Files.parseConfigFile(configFile).get("mainFunction"));
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

        // get files in project folder
        ArrayList<File> filesList = new ArrayList<File>();
        Files.getFiles(projectFolder, filesList);

        // get arendelle files
        ArrayList<String> functionsList = new ArrayList<String>();
        for (File f : filesList) {
            if (f.getName().contains(".arendelle")) {
                String name = Files.getRelativePath(projectFolder, f).replace('/', '.').split(".arendelle")[0];
                functionsList.add(name);
            }
        }
		Collections.sort(functionsList);
		
		// display files in the left drawer
		leftDrawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_listview_item, functionsList));

        // store index (+1 because of the header) of current function for later
        leftDrawerInitialSelection = functionsList.indexOf(Files.getRelativePath(projectFolder, currentFunction).replace('/', '.').split(".arendelle")[0]) + 1;

        // reset last selection
        leftDrawerLastSelection = null;

        // close drawer
        drawerLayout.closeDrawer(leftDrawerListView);

	}

    /** shows dialog for new function */
    private void showNewFunctionDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_new_function, null);
        builder.setView(dialogView);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// check input
				if (((EditText) dialogView.findViewById(R.id.dialog_new_function_text_name)).getText().toString().equals("")) {
					showNewFunctionDialog();
				} else {
					newFunction(((EditText) dialogView.findViewById(R.id.dialog_new_function_text_name)).getText().toString());
				}
			}
		});
        builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});

        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();

    }

    /** creates new function */
    private void newFunction(String functionName) {

        File function;

        if (functionName.split("\\.").length > 1) {

            // create folders for namespaces
            String foldersPath = functionName.substring(0, functionName.lastIndexOf("."));
            foldersPath = foldersPath.replace('.', '/');
            File folders = new File(projectFolder, foldersPath);
            folders.mkdirs();

            // create function file
            function = new File(folders, functionName.substring(functionName.lastIndexOf(".") + 1) + ".arendelle");

        } else {

            // create function file
            function = new File(projectFolder, functionName + ".arendelle");

        }

        // save previous code and create file
        try {
            Files.write(currentFunction, textCode.getText().toString());
            Files.write(function, "");
        } catch (IOException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

        // set new function as current
        currentFunction = function;
        setTitle(currentFunction.getName().split(".arendelle")[0]);

        // read code
        try {
            textCode.setText(Files.read(currentFunction));
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            finish();
        }

        // refresh files list
        onResume();

    }

    /** shows dialog for rename function */
    private void showRenameFunctionDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_rename, null);
        builder.setView(dialogView);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// check input
				if (((EditText) dialogView.findViewById(R.id.dialog_rename_text_name)).getText().toString().equals("")) {
					showRenameFunctionDialog();
				} else {
					renameFunction(((EditText) dialogView.findViewById(R.id.dialog_rename_text_name)).getText().toString());
				}
			}
		});
        builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});

        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();

    }

    /** renames a function */
    private void renameFunction(String newName) {

        File renamedFile;

        if (newName.split("\\.").length > 1) {

            // create folders for namespaces
            String foldersPath = newName.substring(0, newName.lastIndexOf("."));
            foldersPath = foldersPath.replace('.', '/');
            File folders = new File(projectFolder, foldersPath);
            folders.mkdirs();

            // create renamed function file
            renamedFile = new File(folders, newName.substring(newName.lastIndexOf(".") + 1) + ".arendelle");

        } else {

            // create renamed function file
            renamedFile = new File(projectFolder, newName + ".arendelle");

        }

        // update config file if necessary
        if (currentFunction.getAbsolutePath().equals(mainFunction.getAbsolutePath())) try {
            HashMap<String, String> properties = Files.parseConfigFile(configFile);
            properties.put("mainFunction", Files.getRelativePath(projectFolder, renamedFile.getAbsoluteFile()));
            Files.createConfigFile(configFile, properties);
            mainFunction = renamedFile;
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

        currentFunction.renameTo(renamedFile);
        currentFunction = renamedFile;
        setTitle(currentFunction.getName().split(".arendelle")[0]);

        onResume();

    }

    /** deletes current function */
    private void deleteFunction() {

        // throw error if user wants delete the main function
        if (currentFunction.getAbsolutePath().equals(mainFunction.getAbsolutePath())) {
            Toast.makeText(this, R.string.toast_you_cannot_delete_main_function, Toast.LENGTH_LONG).show();
            return;
        }

        currentFunction.delete();
        currentFunction = mainFunction;
        setTitle(currentFunction.getName().split(".arendelle")[0]);
        try {
            textCode.setText(Files.read(currentFunction));
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            finish();
        }
        onResume();

    }

    /** updates the project */
    private void updateProject(int fromVersion) {

        switch (fromVersion) {

            case 0:
            case 1:
            case 2:
            case 3:
            case 4:

                // convert int colors to hex values
                try {
                    HashMap<String, String> properties = Files.parseConfigFile(configFile);

                    switch (Integer.valueOf(properties.get("colorPalette"))) {

                        // Arendelle Classic
                        case 0:
                            properties.put("colorBackground", "#000000");
                            properties.put("colorFirst", "#FFFFFF");
                            properties.put("colorSecond", "#CECECE");
                            properties.put("colorThird", "#8C8A8C");
                            properties.put("colorFourth", "#424542");
                            break;

                        // Sparkling Blue
                        case 1:
                            properties.put("colorBackground", "#000000");
                            properties.put("colorFirst", "#49CEE6");
                            properties.put("colorSecond", "#49B3E6");
                            properties.put("colorThird", "#499EE6");
                            properties.put("colorFourth", "#4985E6");
                            break;

                        // Arendelle Pink
                        case 2:
                            properties.put("colorBackground", "#000000");
                            properties.put("colorFirst", "#E60087");
                            properties.put("colorSecond", "#B800AD");
                            properties.put("colorThird", "#8E00D7");
                            properties.put("colorFourth", "#6600FF");
                            break;

                        // Simple Red
                        case 3:
                            properties.put("colorBackground", "#FFFFFF");
                            properties.put("colorFirst", "#E70D20");
                            properties.put("colorSecond", "#EC444B");
                            properties.put("colorThird", "#F17E81");
                            properties.put("colorFourth", "#F7BBBE");
                            break;

                        // White Legacy
                        case 4:
                            properties.put("colorBackground", "#EAEAEA");
                            properties.put("colorFirst", "#030303");
                            properties.put("colorSecond", "#313131");
                            properties.put("colorThird", "#6D6D6D");
                            properties.put("colorFourth", "#B3B3B3");
                            break;

                    }

                    Files.createConfigFile(configFile, properties);
                } catch (Exception e) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                    finish();
                }

                // NO BREAK!

            default:
                break;

        }

    }

}