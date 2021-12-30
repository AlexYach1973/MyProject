package com.example.android.myproject.schedule;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.myproject.database.ScheduleDao;
import com.example.android.myproject.database.ScheduleDatabase;
import com.example.android.myproject.database.ScheduleEntity;

import java.util.ArrayList;
import java.util.List;

public class ScheduleViewModel extends ViewModel {

    private final ScheduleDao scheduleDao;

    /**
     * Наблюдаемые данные для двусторонней привязки
     */
    // Type
    private String obsType = "";
    public String getObsType() {
        return obsType;
    }
    public void setObsType(String value) {
            obsType = value;
    }

    // TimeHour
    private String timeHour = "";
    public String getTimeHour() {
        return timeHour;
    }
    public void setTimeHour(String value) {
        timeHour = value;
    }

    // TimeMinute
    private String timeMinute = "";
    public String getTimeMinute() {
        return timeMinute;
    }
    public void setTimeMinute(String value) {
        timeMinute = value;
    }

    // Amount
    private String amount = "";
    public String getAmount() {
        return amount;
    }
    public void setAmount(String value) {
        amount = value;
    }

    // Description
    private String description = "";
    public String getDescription() {
        return description;
    }
    public void setDescription(String value) {
        description = value;
    }

    /**
     * Конец наблюдаемых данных для двусторонней привязки
     */

    // Список
    private LiveData<List<ScheduleEntity>> allScheduleEntity = new MutableLiveData<>();
    public LiveData<List<ScheduleEntity>> getAllScheduleEntity() {
        return allScheduleEntity;
    }

    // Boolean переменная показ/скрыть поля вставки
    private MutableLiveData<Boolean> visibleInsert = new MutableLiveData<>();
    public LiveData<Boolean> getVisibleInsert() {
        return visibleInsert;
    }

    // Boolean- переменная вывода Тостов из Фрагмента
    private MutableLiveData<Integer> toastShow = new MutableLiveData<>();
    public LiveData<Integer> getToastShow() {
        return toastShow;
    }
    public void setToastShow(int value) {
        toastShow.setValue(value);
    }

    // Коонструктор
    public ScheduleViewModel(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
        // Прочитали все
        allScheduleEntity = scheduleDao.getAll();

        // Установили Boolean переменные
        visibleInsert.setValue(false);
//        toastShow.setValue(0);
    }

    // Изменение Boolean переменной видимости поля вставки
    public void inverseVisiblyInsert() {

        if (visibleInsert.getValue() != null) {
            if (visibleInsert.getValue())
                visibleInsert.setValue(false);
            else
                visibleInsert.setValue(true);
        }

        // Очищаем поля ввода - НЕ работает
//        clearFieldEnter();

    }

    private void clearFieldEnter() {
        setObsType("");
        setTimeHour("");
        setTimeMinute("");
        setAmount("");
    }

    // Функция Записи новых данных
    public void addNewInjection() {

        // Проверка полей при вводе
        // На нулевые поля
        if (isEntryValid()) {
            // Часы и минуты
            if (isHourAnfMinuteValid(timeHour, 24) &&
                    isHourAnfMinuteValid(timeMinute, 60) ) {

                // формируем строку Время из часов и минут
                String strTimeBuild = timeHour + ":" + timeMinute;

                // Формируем новый экземпляр ScheduleEntity
                ScheduleEntity scheduleEntity =
                        new ScheduleEntity(obsType, strTimeBuild, amount,
                                description, false);

                // Вставляем в БД
                insert(scheduleEntity);
            }
        }

        // Очищаем поля ввода - НЕ работает
//        clearFieldEnter();

    }

    // Функция проверки установки времени
    private boolean isHourAnfMinuteValid(String strTime, int count) {

        if (strTime.contains(".") || strTime.contains(",") ||
                strTime.contains("-") || strTime.contains(" ")) {
            // Для вывода Toast
            setToastShow(1);
            return false;

        } else if (Integer.parseInt(strTime) < 0 || Integer.parseInt(strTime) > count) {
            // Для вывода Toast
            setToastShow(2);
            return false;
        }
        return true;
    }

    // Проверка на не нулевые поля Записи новых данных (кроме description)
    private Boolean isEntryValid() {
        if (getObsType().equals("") || getTimeHour().equals("") || getTimeMinute().equals("") ||
                getAmount().equals("")) {

            // Для вывода Toast
            setToastShow(3);
            return false;
        }

        return true;
    }

    // Вставка в фоновом потоке
    public void insert(ScheduleEntity scheduleEntity) {
        ScheduleDatabase.databaseWriteExecutor.execute(() ->
                scheduleDao.insertInjection(scheduleEntity));
    }

    // Удаление всего в фоновом потоке
    public void deleteAll() {
        ScheduleDatabase.databaseWriteExecutor.execute(scheduleDao::deleteAll);
    }

    // UpDate поля удаления елемента
    public void upDate(ScheduleEntity scheduleEntity){

        // Меняем Boolean- переменную удаления элемента
        scheduleEntity.setBooleanValue(!scheduleEntity.getBooleanValue());

        // Обновляем элемент в фоновом потоке
        ScheduleDatabase.databaseWriteExecutor.execute(() ->
                scheduleDao.updateInjection(scheduleEntity));
    }


    // Удаление выделенных элементов
    public void deleteOneElement() {
        ArrayList<Integer> listId = new ArrayList();

        // Ищем отмеченные элементы
        if (allScheduleEntity.getValue() != null) {
            for (ScheduleEntity sch : allScheduleEntity.getValue()) {

                // Собираем Id отмеченных элементов
                if (sch.getBooleanValue()) {
                    listId.add(sch.getId());
                }
            }
        }

        // Удаляем выбранные элементы
        ScheduleDatabase.databaseWriteExecutor.execute(() ->
                scheduleDao.deleteByIdList(listId));
    }



}