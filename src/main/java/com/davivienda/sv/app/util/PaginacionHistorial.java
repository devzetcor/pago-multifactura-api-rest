package com.davivienda.sv.app.util;

import java.util.ArrayList;
import java.util.List;

public class PaginacionHistorial<T>
{
    private int tamanoPagina;
    private int currentPage;
    private List<T> data;
    
    public PaginacionHistorial() {
        this.tamanoPagina = 0;
        this.currentPage = 0;
        this.data = new ArrayList<T>();
    }
    
    public List<T> getCurrentPageData() {
         int startIndex = this.currentPage;
         int endIndex = Math.min(startIndex + this.tamanoPagina, this.data.size());
        return new ArrayList<T>(this.data.subList(startIndex, endIndex));
    }
    
    public int getCurrentPage() {
        return this.currentPage;
    }
    
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    
    public List<T> getData() {
        return this.data;
    }
    
    public void setData(List<T> data) {
        this.data = data;
    }
    
    public int getTamanoPagina() {
        return this.tamanoPagina;
    }
    
    public void setTamanoPagina(int tamanoPagina) {
        this.tamanoPagina = tamanoPagina;
    }
    
    public int getTotalPages() {
        return (int)Math.ceil(this.data.size() / (double)this.tamanoPagina);
    }
    
}
