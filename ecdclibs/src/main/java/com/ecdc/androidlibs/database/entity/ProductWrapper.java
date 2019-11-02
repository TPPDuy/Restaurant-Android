package com.ecdc.androidlibs.database.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductWrapper implements Serializable
{
    @SerializedName("docs")
    private List<Product> products;

    public ProductWrapper()
    {
        products = new ArrayList<>();
    }
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products)
    {
        this.products = products;
    }

    public void addProduct(Product product)
    {
        if (isExisted(product) == -1)
            products.add(product);
    }

    private int isExisted(Product product)
    {
        for(Product i : products)
        {
            if (i.getId().equals(product.getId()))
                return products.indexOf(i);
        }
        return -1;
    }

    public int getQuantity()
    {
        int result = 0;
        for(Product i : products)
            result += i.getQuantity();
        return result;
    }

    public Product getProductById(String id)
    {
        for (Product i : products)
        {
            if (i.getId().equals(id))
                return i;
        }
        return null;
    }
}
