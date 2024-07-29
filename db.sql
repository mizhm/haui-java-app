use master
go

create database coffee_shop
go

use coffee_shop
go

create table category
(
    id         int identity,
    name       nvarchar(255) unique not null,
    status     bit default (1),
    created_at datetime             not null,
    updated_at datetime,
    primary key (id)
)
go

create table product
(
    id          int identity,
    name        nvarchar(255) unique not null,
    price       float                not null,
    status      bit default (1),
    created_at  datetime             not null,
    updated_at  datetime             not null,
    category_id int                  not null,
    primary key (id),
    foreign key (category_id) references category (id)
)
go

create table bill
(
    id         int identity,
    status     bit default (1),
    created_at datetime not null,
    updated_at datetime,
    primary key (id)
)
go

create table bill_detail
(
    bill_id    int not null,
    product_id int not null,
    amount     int not null,
    primary key (bill_id, product_id),
    foreign key (bill_id) references bill (id),
    foreign key (product_id) references product (id)
)

go

create proc usp_insert_category(
    @_name nvarchar(255),
    @_status bit = 1,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar(255) = '' output
)
as
begin try
    if exists(select name from category where name = @_name)
        begin
            set @_out_stt = 0;
            set @_out_msg = N'Category exists!';
        end
    else
        begin
            begin tran;
            insert into category(name, status, created_at, updated_at)
            values (@_name, @_status, getdate(), getdate());
            set @_out_stt = 1;
            set @_out_msg = N'Insert successfully';
            if @@trancount > 0
                commit tran;
        end
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = error_message();
    if @@trancount > 0
        rollback tran;
end catch
go

exec usp_insert_category @_name= N'Coffee'
go
exec usp_insert_category @_name= N'Tea'
gO
exec usp_insert_category @_name= N'Juice'
go
exec usp_insert_category @_name= N'Cake'
go

create proc usp_update_category(
    @_id int,
    @_name nvarchar(255),
    @_status bit,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar(255) = '' output
)
as
begin try
    if not exists(select * from category where id = @_id)
        begin
            set @_out_stt = 0;
            set @_out_msg = N'Category to update not valid';
        end
    else
        begin
            if exists(select * from category where name = @_name and id <> @_id)
                begin
                    set @_out_stt = 0;
                    set @_out_msg = N'Category name already in use';
                end
            else
                begin
                    begin tran;
                    update category
                    set name= @_name,
                        status = @_status,
                        updated_at = getdate()
                    where id = @_id;
                    set @_out_stt = 1;
                    set @_out_msg = n'update successully';
                    if @@trancount > 0
                        commit tran;
                end
        end
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = error_message();
    if @@trancount > 0
        rollback tran;
end catch
go

create proc usp_delete_category(
    @_id int,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar = '' output
)
as
begin try
    if exists (select *
               from category
                        join product on product.category_id = category.id
               where category.id = @_id)
        begin
            set @_out_stt = 0;
            set @_out_msg = N'category has product'
        end
    else
        begin
            begin tran;
            delete category
            where id = @_id;
            set @_out_stt = 1;
            set @_out_msg = N'delete successfully';
            if @@trancount > 0
                commit tran;
        end
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = error_message();
    if @@trancount > 0
        rollback tran;
end catch
go

create proc usp_get_all_category(
    @_name nvarchar(255) = null,
    @_status bit = null
)
as
begin
    declare @sql nvarchar(max) = 'select * from category where 1 = 1';
    if (@_name is not null)
        set @sql = concat(@sql, ' and name like ''%', @_name, '%''');
    if (@_status is not null)
        set @sql = concat(@sql, ' and status = ', @_status);
    exec (@sql)
end
go

create proc usp_get_category_by_id(
    @_id int
) as
begin
    select top 1 * from category where id = @_id
end
go

create proc usp_insert_product(
    @_name nvarchar(255),
    @_status bit = 1,
    @_price float,
    @_category_id int,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar(255) = '' output
) as
begin try
    if not exists(select * from category where id = @_category_id)
        begin
            set @_out_stt = 0;
            set @_out_msg = 'Category not exists!';
        end
    else
        begin
            if exists(select * from product where lower(name) = lower(@_name))
                begin
                    set @_out_stt = 0;
                    set @_out_msg = 'Product exists'
                end
            else
                begin
                    begin tran;
                    insert into product(name, status, price, category_id, created_at, updated_at)
                    values (@_name, @_status, @_price, @_category_id, getdate(), getdate());
                    set @_out_stt = 1;
                    set @_out_msg = 'Create successfully'
                    if @@trancount > 0
                        commit tran
                end
        end
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = error_message()
    if @@trancount > 0
        rollback tran
end catch
go

create proc usp_update_product(
    @_id int,
    @_name nvarchar(255),
    @_status bit = 1,
    @_price float,
    @_category_id int,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar(255) = '' output
) as
begin try
    if not exists(select * from product where id = @_id)
        begin
            set @_out_stt = 0;
            set @_out_msg = 'Product not exists';
        end
    else
        begin
            if exists(select * from product where name = @_name and id <> @_id)
                begin
                    set @_out_stt = 0;
                    set @_out_msg = 'Product name exists';
                end
            else
                begin
                    begin tran;
                    update product
                    set name= @_name,
                        status = @_status,
                        price = @_price,
                        category_id = @_category_id,
                        updated_at= getdate()
                    where id = @_id;
                    set @_out_stt = 1;
                    set @_out_msg = 'Update successfully!'
                    if @@trancount > 0
                        commit tran
                end
        end
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = error_message();
    if @@trancount > 0
        rollback tran
end catch
go

create proc usp_delete_product(
    @_id int,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar(255) = '' output
) as
begin try
    if exists(select *
              from product
                       join bill_detail on bill_detail.product_id = product.id
              where id = @_id)
        begin
            set @_out_stt = 0;
            set @_out_msg = 'Product exists in some bills';
        end
    else
        begin
            begin tran;
            delete product
            where id = @_id;
            set @_out_stt = 1;
            set @_out_msg = 'Delete successfully';
            if @@trancount > 0
                commit tran
        end
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = error_message()
    if @@trancount > 0
        rollback tran
end catch
go

create proc usp_get_all_product(
    @_name nvarchar = null,
    @_status bit = null
) as
begin
    declare @sql nvarchar(max) = 'select * from product where 1=1';
    if @_name is not null
        set @sql = concat(@sql, ' and name like ''%', @_name, '%''');
    if @_status is not null
        set @sql = concat(@sql, ' and status = ', @_status);
    exec (@sql);
end
go

create proc usp_get_product_by_id(
    @_id int
) as
begin
    select top 1 * from product where id = @_id
end
go

create proc usp_create_bill(
    @_status bit,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar = '' output
)
as
begin try
    begin tran;
    insert into bill(status, created_at, updated_at) values (@_status, getdate(), getdate());
    set @_out_stt = 1;
    set @_out_msg = N'Create Bill successfully';
    if @@trancount > 0
        commit tran
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = error_message();
    if @@trancount > 0
        rollback tran
end catch
go

create proc usp_update_bill(
    @_id int,
    @_status bit,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar(255) = '' output
)
as
begin try
    if not exists(select * from bill where id = @_id)
        begin
            set @_out_stt = 0;
            set @_out_msg = N'Bill not exist';
        end
    else
        begin
            begin
                begin tran;
                update bill
                set status     = @_status,
                    updated_at = getdate()
                where id = @_id;

                set @_out_stt = 1;
                set @_out_msg = n'update successully';
                if @@trancount > 0
                    commit tran;
            end
        end
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = error_message();
    if @@trancount > 0
        rollback tran;
end catch
go

create proc usp_create_bill_detail(
    @_bill_id int,
    @_product_id int,
    @_amount int,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar = '' output
)
as
begin try
    if not exist(select * from bill where id = @_bill_id)
        begin
            set @_out_stt = 0;
            set @_out_msg = N'Bill not exist';
        end
    else
        if not exist(select * from product where id = @_product_id)
            begin
                set @_out_stt = 0;
                set @_out_msg = N'Product not exist';
            end
        else
            begin
                if @_amount <= 0
                    begin
                        set @_out_stt = 0;
                        set @_out_msg = N'Amount not valid';
                    end
                else
                    begin
                        begin tran;
                        insert into bill_detail(bill_id, product_id, amount) values (@_bill_id, @_product_id, @_amount)
                        set @_out_stt = 1;
                        set @_out_msg = N'Create successfully';
                        if @@trancount > 0
                            commit tran;
                    end
            end
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = error_message();
    if @@trancount > 0
        rollback tran;
end catch
go

create proc usp_get_bill(
    @_status bit
)
as
begin
    declare @sql nvarchar(max)= N'select * from bill where 1=1';
    if @_status is not null
        set @sql = concat(@sql, 'and status = ', @_status);
    exec (@sql);
end
go

create proc usp_get_bill_with_detail(
    @_id int,
    @_status bit
)
as
begin
    declare @sql nvarchar(max) = concat(
            N'select * from bill join bill_detail on bill.id = bill_detail.bill_id where bill.id = ', @_id)
    if @_status is not null
        set @sql = concat(@sql, ' and bill.status = ', @_status)
    exec (@sql);
end
go