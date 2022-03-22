package com.example.android.myproject.dimension;

import static android.view.MenuItem.SHOW_AS_ACTION_ALWAYS;
import static android.view.MenuItem.SHOW_AS_ACTION_NEVER;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.myproject.R;
import com.example.android.myproject.databinding.DimensionFragmentBinding;

public class DimensionFragment extends Fragment {
    // binding
    private DimensionFragmentBinding binding;
    // Context
    private Application application;
    // ViewModel
    private DimensionViewModel dimensionViewModel;

    /** Date */
    String strDate;

    /** DataBase */
    DataBaseHelper dataBaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

    // Id выбраного элемента
//    long currentId = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Получение результатов с помощью Fragment Result API
         */
        requireActivity().getSupportFragmentManager().setFragmentResultListener("Key",
                this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                        // Извлекаем данные
                        int day = result.getInt("day");
                        int month = result.getInt("month");
                        int year = result.getInt("year");

                        // Че-то делаем с данными

                        // Формируем дату- типа String
                        strDate = formDateToString(day, (month + 1), year);

                        // Назначаем дату TextView через ViewModel
                        // Срабатывает только после переворота
                        dimensionViewModel.setSelectedDate(strDate);

                        // Назначаем дату непосредственно в TextView
                        // Что бы сразу видеть
                        binding.textViewDataDimen.setText(strDate);
                    }
                });
        /** ****************************** */
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // binding
        binding = DimensionFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Владелец жизненого цикла
        binding.setLifecycleOwner(this);

        // Get Application
        application = requireActivity().getApplication();

        // ViewModel
        dimensionViewModel = new ViewModelProvider(this).get(DimensionViewModel.class);

        // Привязка dimension_fragment.XML с ViewModel
        binding.setViewModel(dimensionViewModel);

        /** DataBase */
        dataBaseHelper = new DataBaseHelper(application);
        db = dataBaseHelper.getReadableDatabase();

        /** Adapter */
        // Чтение информации и установка адаптера
        readAndSetAdapter();

        /** Слушатели */

        //*****************************//
        // Касание элемента listView
        binding.listviewDimen.setOnItemClickListener((parent, view1, position, currentId) -> {

            // Назначаем currentId
            dimensionViewModel.setCurrentId(currentId);

            // выбраный элемент
                chooseElement(view1, position, currentId);

//            Log.d("myLogs", "int position= " + position + ", long id= " + id);
        });

        //*****************************//
        // Button Добавить
        binding.buttonAddDimen.setOnClickListener(v -> {
            // Вставка новой строки
            insertNewItem();
        });

        //*****************************//
        // Button Удаление
        binding.buttonDeleteDimen.setOnClickListener(v -> {

            // Удаляем выбранный элемент по id
                int deleteCont = dimensionViewModel
                        .deleteById(db, String.valueOf(dimensionViewModel.getCurrentId()));

                // Обнуляем currentId
            dimensionViewModel.setCurrentId(0);

//                Log.d("myLogs",  "currentId= " + dimensionViewModel.getCurrentId() +
//                        "; deleteCont = " + deleteCont);

            // Проверяем - удалили / не удалили
            if (deleteCont > 0) {
                Toast.makeText(application, "Удалена строка с id= " +
                        dimensionViewModel.getCurrentId(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(application, "Удалить не получилось" ,
                        Toast.LENGTH_SHORT).show();
            }

            // Заново считываем базу данных и обновляем listView
            readAndSetAdapter();
        });

        //*****************************//
        // Слушатель поля TextView DATE
        binding.textViewDataDimen.setOnClickListener(v -> {

            // Запускает TimeDialogFragment
            showTimePickerDialog();

        });
        /** ********** */

        /**  Наблюдения */

        // Наблюдаем за булен- переменной, чтобы установить состояние кнопки Добавить
        dimensionViewModel.getVisibleAdd().observe(getViewLifecycleOwner(), bool -> {
            // Устанавливаем кликабельность кнопки
            binding.buttonAddDimen.setClickable(bool);

            if (bool) {
                binding.buttonAddDimen.setTextColor(getResources().getColor(R.color.white));
            } else {
                binding.buttonAddDimen.setTextColor(getResources().getColor(R.color.purple_200));
            }
        });

        // Наблюдаем за булен- переменной, чтобы установить состояние кнопки Удалить
        dimensionViewModel.getVisibleDelete().observe(getViewLifecycleOwner(), bool -> {
            // Устанавливаем кликабельность кнопки
            binding.buttonDeleteDimen.setClickable(bool);

//            Log.d("myLogs", "getVisibleDelete().observe = " + bool);

            // Устанавливаем текст кнопки
            if (!bool) {
                binding.buttonDeleteDimen.setText(getResources().getText(R.string.button_delete));
            }

            // Устанавливаем цвет надписи
            if (bool) {
                binding.buttonDeleteDimen.setTextColor(getResources().getColor(R.color.white));
            } else {
                binding.buttonDeleteDimen.setTextColor(getResources().getColor(R.color.purple_200));
            }
        });

        /** ********** */



        // Разрешает использовать свое меню
        setHasOptionsMenu(true);
        return view;
    }


    // Что делать с выбранным элементом
    private void chooseElement(View view, int position, long id) {

        // Если активна кнопка Удалить
        // Меняем надпись на кнопке Удалить

        if (dimensionViewModel.getVisibleDelete().getValue() != null) {
            if (dimensionViewModel.getVisibleDelete().getValue() &&
                    dimensionViewModel.getCurrentId() != 0) {

                String str = getResources().getText(R.string.button_delete) + " id = " +
                        dimensionViewModel.getCurrentId();
                binding.buttonDeleteDimen.setText(str);
            }
        }

    }


    // Чтение информации и установка адаптера
    private void readAndSetAdapter() {
        // Чтение из БД
        userCursor = dimensionViewModel.readAllList(db);
        // перенес в ViewModel
//        userCursor = db.rawQuery("select * from " + DataBaseHelper.TABLE, null);

        /** Adapter */
        // Массив from[]
        String[] headers = new String[] {DataBaseHelper.COLUMN_ID,
                DataBaseHelper.COLUMN_DATE, DataBaseHelper.COLUMN_VALUE};

        // Adapter.
        // SimpleCursorAdapter(context, layout, cursor, from[], to[], flag)
        userAdapter = new SimpleCursorAdapter(application,
                R.layout.dimension_item,
                userCursor,
                headers,
                new int[]{R.id.item_id_dimen, R.id.item_date_dimen, R.id.item_value_dimen},
                0);

        // Цепляем адаптер на listView
        binding.listviewDimen.setAdapter(userAdapter);
    }

    // Запускает TimeDialogFragment
    public void showTimePickerDialog() {
        DialogFragment dataFragment = new TimeDialogFragment();
        dataFragment.show(requireActivity().getSupportFragmentManager(),"Data");
    }

    // Формируем дату- типа String
    private String formDateToString(int day, int month, int year) {
       String dayStr = (day < 10)? "0" + day : String.valueOf(day);
       String monthStr = (month < 10)? "0" + month : String.valueOf(month);

       return dayStr + "." + monthStr + "." + year;
    }

    /** MENU ** */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.dimension_menu, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            // Delete All
            case R.id.menu_delete_all_dimen:
                dimensionViewModel.deleteAll(db);
                // Заново считываем и обновляем listView
                readAndSetAdapter();
                break;

                // Удаление одного элемента
            case R.id.menu_delete_dimen:
                dimensionViewModel.inverseVisiblyDelete();
                // Меняем цвет иконки
                iconSwitchDelete(item);
                break;

            // Insert
            case R.id.menu_insert_dimen:
                // Меняем значение булен- переменной
                dimensionViewModel.inverseVisiblyAdd();

                // Меняем цвет иконки
                iconSwitchAdd(item);
                break;

            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    // Меняем располодение иконки
    private void iconSwitchDelete(MenuItem item) {
        if (dimensionViewModel.getVisibleDelete().getValue() != null) {

            if (dimensionViewModel.getVisibleDelete().getValue()) {
                item.setIcon(R.drawable.delete_item);
                item.setShowAsAction(SHOW_AS_ACTION_ALWAYS);

            } else {
//                item.setIcon(R.drawable.delete_item_w);
                item.setShowAsAction(SHOW_AS_ACTION_NEVER);
                // Обнуляем currentId
                dimensionViewModel.setCurrentId(0);
            }
        }
    }

    // Меняем расположение иконки
    private  void iconSwitchAdd(MenuItem item) {
        if (dimensionViewModel.getVisibleAdd().getValue() != null) {

            if (dimensionViewModel.getVisibleAdd().getValue()) {
                item.setIcon(R.drawable.insert_true);
                item.setShowAsAction(SHOW_AS_ACTION_ALWAYS);

            } else {
                item.setShowAsAction(SHOW_AS_ACTION_NEVER);
//                item.setIcon(R.drawable.insert_24);
            }
        }

    }

    // Вставка новой строки
    private void insertNewItem() {
        ContentValues cv = new ContentValues();
        String strValue = binding.editTextValueDimen.getText().toString();

        // Проверяем на пустое поле
        if (isValidValue(strValue)) {
            return;
        }
        // Проверка на пустое поле даты
        if (isValidDate()) {
            return;
        }

        // Вставляем в ContentValues текст из EditText
        cv.put(DataBaseHelper.COLUMN_DATE, strDate);
        cv.put(DataBaseHelper.COLUMN_VALUE, strValue);

        // Вставляем в db
        long rowId = db.insert(DataBaseHelper.TABLE, null, cv);
        // Сообщаем
        if (rowId > 0) {
            Toast.makeText(application, "Элемент вставлен с id= " + rowId,
                    Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(application, "Что-то пошло не так",
                    Toast.LENGTH_LONG).show();
        }

        // Заново считываем и обновляем listView
        readAndSetAdapter();

        // Очищаем поля
        binding.editTextValueDimen.setText("");
        binding.textViewDataDimen.setText("");

    }

    // Проверяем на пустое поле
    private boolean isValidValue(String strValue) {
        if (strValue.isEmpty()){
            Toast.makeText(application,"Введите значение !!!",
                    Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }
    // Проверка на пустое поле даты
    private boolean isValidDate() {
        if (strDate == null){
            Toast.makeText(application,"Введите дату !!!",
                    Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }



}